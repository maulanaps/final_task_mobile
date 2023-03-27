package com.example.submission03

import android.content.Intent
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.submission03.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Login"

        binding.apply {
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()

                val sharedPref = getSharedPreferences("LOGIN", MODE_PRIVATE)
                val editor = sharedPref.edit()

                if (username.isEmpty() || password.isEmpty()) {
                    Snackbar.make(constraintLayout, "Please fill the username and password", 2000)
                        .show()
                } else {
                    editor.apply {
                        putString("username", username)
                        putString("password", password)
                        putBoolean("login", true)

                        editor.apply()
                    }

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    companion object {
        fun open(activity: AppCompatActivity) {
            val intent = Intent(activity, LoginActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}