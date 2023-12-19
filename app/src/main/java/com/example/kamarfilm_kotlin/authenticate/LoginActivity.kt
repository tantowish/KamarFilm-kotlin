package com.example.kamarfilm_kotlin.authenticate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kamarfilm_kotlin.R
import com.example.kamarfilm_kotlin.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}