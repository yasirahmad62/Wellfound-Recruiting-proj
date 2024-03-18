package com.example.wellfoundrecruiting

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CandidateDetailActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_detail)

        val candidateId = intent.getStringExtra("candidate_id")
        database = FirebaseDatabase.getInstance().getReference("users").child(candidateId.toString())

        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val cityTextView = findViewById<TextView>(R.id.cityTextView)
        val educationTextView = findViewById<TextView>(R.id.educationTextView)
        val userImageView = findViewById<ImageView>(R.id.userImageView)
        val addConnectionButton = findViewById<FloatingActionButton>(R.id.addConnectionButton)
        val connectionStatusTextView = findViewById<TextView>(R.id.connectionStatusTextView)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java)
                val title = snapshot.child("title").getValue(String::class.java)
                val city = snapshot.child("city").getValue(String::class.java)
                val educationDegree = snapshot.child("education_history").child("degree").getValue(String::class.java)
                val graduationYear = snapshot.child("education_history").child("graduation_year").getValue(Int::class.java)
                val university = snapshot.child("education_history").child("university").getValue(String::class.java)
                val photoUrl = snapshot.child("photo_url").getValue(String::class.java)

                nameTextView.text = name
                titleTextView.text = title
                cityTextView.text = "$city"
                educationTextView.text = getString(R.string.education_info, educationDegree, university, graduationYear)

                // Load user image from Firebase Storage using Glide
                Glide.with(this@CandidateDetailActivity)
                    .load(photoUrl)
                    .into(userImageView)

                val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserUid != null) {
                    val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserUid)
                    userRef.child("connections").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val isUserConnected = snapshot.exists() && snapshot.children.any { it.getValue(String::class.java) == candidateId }

                            if (isUserConnected) {
                                connectionStatusTextView.visibility = View.VISIBLE
                                addConnectionButton.visibility = View.GONE
                            } else {
                                connectionStatusTextView.visibility = View.GONE
                                addConnectionButton.visibility = View.VISIBLE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        addConnectionButton.setOnClickListener {
            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
            if (currentUserUid != null) {
                val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserUid)
                userRef.child("connections").push().setValue(candidateId)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@CandidateDetailActivity, "Candidate added to connections", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@CandidateDetailActivity, "Failed to add candidate to connections", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
