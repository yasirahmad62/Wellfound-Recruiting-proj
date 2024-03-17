package com.example.wellfoundrecruiting
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

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
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                candidates.clear()
                for (candidateSnapshot in snapshot.children) {
                    // Following the logic to show just the randomly populated data in the data base
                    if (candidateSnapshot.key?.startsWith("candidate_id") == true) {
                        val candidate = candidateSnapshot.getValue(Candidate::class.java)
                        candidate?.let { candidates.add(it) }
                    }
                }
                adapter.notifyDataSetChanged()
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
