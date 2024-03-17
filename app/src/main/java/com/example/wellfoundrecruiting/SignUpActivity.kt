package com.example.wellfoundrecruiting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextFirstName = findViewById<EditText>(R.id.editTextFirstName)
        val buttonSignup = findViewById<Button>(R.id.buttonSignup)
        val loginBtn = findViewById<Button>(R.id.buttonLogin)

        buttonSignup.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val firstName = editTextFirstName.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            if (user != null) {
                                val database = FirebaseDatabase.getInstance()
                                val userRef = database.getReference("users").child(user.uid)
                                userRef.child("firstName").setValue(firstName)
                            } else {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "User is null",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Sign up failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please enter email, password, and first name",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
