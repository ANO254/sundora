package com.example.sundora

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    lateinit var gotolog: TextView
    lateinit var edt_email: EditText
    lateinit var edt_pass: EditText
    lateinit var conf_pass: EditText
    lateinit var usrname: EditText
    lateinit var signup: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // View Bindings
        edt_email = findViewById(R.id.edt_email)
        edt_pass = findViewById(R.id.edt_pwd)
        conf_pass = findViewById(R.id.edt_confpass)
        usrname = findViewById(R.id.edt_fullname)
        signup = findViewById(R.id.btn_reg)
        auth = Firebase.auth

        gotolog = findViewById(R.id.txt_gotolog)
        gotolog.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signup.setOnClickListener {
            SignupUser()
        }
    }

    private fun SignupUser() {
        val email = edt_email.text.toString()
        val pass = edt_pass.text.toString()
        val confpass = conf_pass.text.toString()

        if (email.isBlank() || pass.isBlank() || confpass.isBlank()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_LONG).show()
            return
        } else if (pass != confpass) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG)
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Account created Successfully", Toast.LENGTH_LONG).show()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to create account", Toast.LENGTH_LONG).show()
            }
        }
    }
}