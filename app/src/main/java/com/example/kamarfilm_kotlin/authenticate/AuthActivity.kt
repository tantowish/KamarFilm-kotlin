package com.example.kamarfilm_kotlin.authenticate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.kamarfilm_kotlin.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            viewPagerLogin.adapter = AuthAdapter(supportFragmentManager)
            // Hubungkan ViewPager dengan TabLayout
            tabLayoutLogin.setupWithViewPager(viewPagerLogin)
        }
    }
}