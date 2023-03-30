package com.example.submission05.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.submission03.databinding.ActivityLoginBinding
import com.example.submission05.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Login"

        binding.apply {
            btnLogin.setOnClickListener {
                val sharedPref = getSharedPreferences("LOGIN", MODE_PRIVATE)
                val editor = sharedPref.edit()

                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()

                val usernameSaved = sharedPref.getString("username", null)
                val passwordSaved = sharedPref.getString("password", null)

                // username / password empty
                if (username.isEmpty() || password.isEmpty()) {
                    Snackbar.make(constraintLayout, "Please fill the username and password", 2000)
                        .show()
                }

                // username match
                else if (username == usernameSaved) {
                    // password match (login)
                    if (password == passwordSaved) {
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()

                        // redirect to main activity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    // password mismatch
                    else {
                        Snackbar.make(constraintLayout, "Wrong password", 2000)
                            .show()
                    }

                // regis new user
                } else {
                    editor.putString("username", username)
                    editor.putString("password", password)
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    // redirect to main activity
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