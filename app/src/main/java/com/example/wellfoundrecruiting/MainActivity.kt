package com.example.wellfoundrecruiting

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstName = intent.getStringExtra("firstName")
        val textViewWelcome = findViewById<TextView>(R.id.textViewWelcome)

        if (firstName != null) {
            textViewWelcome.text = "Welcome, $firstName!"
        }
    }
}
