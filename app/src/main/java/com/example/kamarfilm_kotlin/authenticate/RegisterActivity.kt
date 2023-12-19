package com.example.kamarfilm_kotlin.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.kamarfilm_kotlin.admin.DashboardActivity
import com.example.kamarfilm_kotlin.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegist.setOnClickListener {
            if (checkField(binding.email) && checkField(binding.phone) &&
                checkField(binding.password) && checkField(binding.name)
            ) {
                auth.createUserWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(
                            this@RegisterActivity,
                            "Account Created",
                            Toast.LENGTH_SHORT
                        ).show()

                        val df: DocumentReference =
                            firestore.collection("Users").document(user?.uid ?: "")
                        val userInfo: MutableMap<String, Any> = mutableMapOf(
                            "name" to binding.name.text.toString(),
                            "email" to binding.email.text.toString(),
                            "phone" to binding.phone.text.toString(),
                            "role" to "user"
                        )
                        df.set(userInfo)
                        val intent =
                            Intent(this@RegisterActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Failed to Create Account: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Input the needed credentials.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun checkField(editText: EditText): Boolean {
        return editText.text.toString().trim().isNotEmpty()
    }
}
