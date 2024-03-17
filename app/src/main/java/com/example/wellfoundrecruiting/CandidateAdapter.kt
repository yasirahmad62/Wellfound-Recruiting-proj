package com.example.wellfoundrecruiting
import Candidate
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class CandidateAdapter(private val candidates: List<Candidate>) :
    RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>() {

    inner class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImageView: ImageView = itemView.findViewById(R.id.userImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val cityTextView: TextView = itemView.findViewById(R.id.titleCityView)

        fun bind(candidate: Candidate) {
            // Load user image from Firebase Storage using Glide
            Glide.with(itemView)
                .load(candidate.photo_url) // Assuming `photoUrl` is the URL of the user image in Firebase Storage
                .into(userImageView)

            nameTextView.text = candidate.name
            titleTextView.text = candidate.title
            cityTextView.text = candidate.city

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CandidateDetailActivity::class.java)
                intent.putExtra("candidate_id", candidate.id) // Pass candidate ID
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_candidate, parent, false)
        return CandidateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = candidates[position]
        holder.bind(candidate)
    }

    override fun getItemCount(): Int {
        return candidates.size
    }
}

