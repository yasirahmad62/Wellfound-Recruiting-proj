package com.example.wellfoundrecruiting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(emptyList())
        recyclerView.adapter = postAdapter

        database = FirebaseDatabase.getInstance().getReference("users")
        fetchConnectedUsersPosts()
    }

    private fun fetchConnectedUsersPosts() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            val connectedUsersRef = database.child(uid).child("connections")
            connectedUsersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connectedUserIds = snapshot.children.mapNotNull { it.getValue(String::class.java) }
                    val posts = mutableListOf<Post>()
                    connectedUserIds.forEach { userId ->
                        val userFeedRef = database.child(userId).child("feed")
                        userFeedRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(feedSnapshot: DataSnapshot) {
                                feedSnapshot.children.mapTo(posts) { postSnapshot ->
                                    postSnapshot.getValue(Post::class.java)!!
                                }
                                postAdapter = PostAdapter(posts)
                                recyclerView.adapter = postAdapter
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
        }
    }
}
