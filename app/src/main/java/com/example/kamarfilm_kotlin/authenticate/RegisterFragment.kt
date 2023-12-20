package com.example.kamarfilm_kotlin.authenticate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.kamarfilm_kotlin.admin.DashboardActivity
import com.example.kamarfilm_kotlin.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                            requireContext(),
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
                        val intent = Intent(requireContext(), DashboardActivity::class.java)
                        requireActivity().startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to Create Account: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
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