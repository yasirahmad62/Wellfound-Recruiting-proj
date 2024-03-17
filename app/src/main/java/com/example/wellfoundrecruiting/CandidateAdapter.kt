package com.example.wellfoundrecruiting
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CandidateAdapter(private val candidates: List<Candidate>) :
    RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>() {

    inner class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImageView: ImageView = itemView.findViewById(R.id.userImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val cityTextView: TextView = itemView.findViewById(R.id.titleCityView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_candidate, parent, false)
        return CandidateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = candidates[position]

        // Load user image from Firebase Storage using Glide
        Glide.with(holder.itemView)
            .load(candidate.photo_url)
            .into(holder.userImageView)

        holder.nameTextView.text = candidate.name
        holder.titleTextView.text = candidate.title
        holder.cityTextView.text = candidate.city
    }

    override fun getItemCount(): Int {
        return candidates.size
    }
}
