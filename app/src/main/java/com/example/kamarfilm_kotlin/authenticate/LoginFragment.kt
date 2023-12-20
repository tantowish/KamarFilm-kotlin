package com.example.kamarfilm_kotlin.authenticate

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.kamarfilm_kotlin.admin.DashboardActivity
import com.example.kamarfilm_kotlin.databinding.FragmentLoginBinding
import com.example.kamarfilm_kotlin.helper.sharedPreference
import com.example.kamarfilm_kotlin.user.MainActivity
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPreference: sharedPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreference = sharedPreference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        sharedPreference = sharedPreference(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            if (checkField(binding.email) && checkField(binding.password)) {
                auth.signInWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(
                            requireContext(),
                            "Berhasil Login",
                            Toast.LENGTH_SHORT
                        ).show()
                        checkAdmin(user?.uid ?: "")
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to Login: ${task.exception?.message}",
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

    private fun checkAdmin(uid: String) {
        val df: DocumentReference =
            firestore.collection("Users").document(uid)
        df.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Document exists, and you can access its data
                val data = documentSnapshot.data
                Log.d(TAG, "onSuccess: $data")
                if(documentSnapshot.getString("role")=="admin"){
                    val intent = Intent(requireContext(), DashboardActivity::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }
                else{
                    val intent = Intent(requireContext(), MainActivity ::class.java)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }
            } else {
                // Document does not exist
                Log.d(TAG, "onSuccess: Document does not exist")
            }
        }.addOnFailureListener { e ->
            // Handle any errors that occurred during the document fetch
            Log.e(TAG, "Error getting document: $e")
        }
    }

    private fun checkField(editText: EditText): Boolean {
        return editText.text.toString().trim().isNotEmpty()
    }
}