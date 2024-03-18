package com.example.wellfoundrecruiting

import Candidate
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class CandidateAdapter(private val candidates: List<Candidate>) :
    RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>() {

    inner class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImageView: ImageView = itemView.findViewById(R.id.userImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val cityTextView: TextView = itemView.findViewById(R.id.titleCityView)

        fun bind(candidate: Candidate) {
            // Load user image from Firebase Storage using Glide
            val storageReference = FirebaseStorage.getInstance().reference
            println(candidate.photo_url)
            val imageRef = storageReference.child("${candidate.photo_url}.jpeg") // Assuming images are JPEGs
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                Glide.with(itemView)
                    .load(uri)
                    .into(userImageView)
            }.addOnFailureListener { exception ->
                 Log.e("CandidateAdapter", "Error loading image: ${exception.message}")
            }

            nameTextView.text = candidate.name
            titleTextView.text = candidate.title
            cityTextView.text = candidate.city

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, CandidateDetailActivity::class.java)
                intent.putExtra("candidate_id", candidate.id) // Passing candidate ID
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
