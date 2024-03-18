package com.example.wellfoundrecruiting

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val floatingHomeBtn = findViewById<FloatingActionButton>(R.id.floatingHomeBtn)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(emptyList())
        recyclerView.adapter = postAdapter

        database = FirebaseDatabase.getInstance().getReference("users")
        fetchConnectedUsersPosts()

        // Navigate to MainActivity
        floatingHomeBtn.setOnClickListener {
            startActivity(Intent(this, CandidateActivity::class.java))
        }
    }
// Wrote a function to fetch to post of the user who are connected
    private fun fetchConnectedUsersPosts() {
//        getting the currentuser uID
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            val connectedUsersRef = database.child(uid).child("connections")
            connectedUsersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connectedUserIds = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                    val posts = mutableListOf<Post>()
                    if (connectedUserIds.isEmpty()) {
                        // No connections
                        showNoPostsMessage()
                        return
                    }
                    connectedUserIds.forEach { userId ->
                        val userFeedRef = database.child(userId).child("feed")
                        userFeedRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(feedSnapshot: DataSnapshot) {
                                if (feedSnapshot.exists()) {
                                    feedSnapshot.children.mapTo(posts) { postSnapshot ->
                                        postSnapshot.getValue(Post::class.java)!!
                                    }
                                    postAdapter = PostAdapter(posts)
                                    recyclerView.adapter = postAdapter
                                } else {
                                    // No posts in this connection's feed
                                    showNoPostsMessage()
                                }
                                postAdapter = PostAdapter(posts)
                                recyclerView.adapter = postAdapter
                            }
                            override fun onCancelled(error: DatabaseError) {
                                showToast("Error fetching user feed: ${error.message}")
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Error fetching connected users: ${error.message}")
                }
            })

        }
    }
    //Added function to show the message that takes the databaseError as an input parameter

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun showNoPostsMessage() {
        // Clear RecyclerView and show message
        postAdapter = PostAdapter(emptyList())
        recyclerView.adapter = postAdapter
        Toast.makeText(this, "No posts yet", Toast.LENGTH_SHORT).show()
    }
}
