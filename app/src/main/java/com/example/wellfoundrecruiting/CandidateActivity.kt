package com.example.wellfoundrecruiting
import Candidate
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CandidateActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CandidateAdapter
    private var candidates: MutableList<Candidate> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CandidateAdapter(candidates)
        recyclerView.adapter = adapter
        val floatingHomeBtn = findViewById<FloatingActionButton>(R.id.floatingHomeBtn)
        database = FirebaseDatabase.getInstance().getReference("users")
        // Add ValueEventListener to listen for database changes
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                candidates.clear()
                for (candidateSnapshot in snapshot.children) {
                    if (candidateSnapshot.key?.startsWith("candidate_id") == true) {
                        val candidate = candidateSnapshot.getValue(Candidate::class.java)
                        candidate?.let { candidates.add(it) }
                    }
                }
                adapter.notifyDataSetChanged()// Notify adapter of data change
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CandidateActivity, "Database error occurred", Toast.LENGTH_SHORT).show()
            }

        })
        floatingHomeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
