package com.example.wellfoundrecruiting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val locationChip: Chip = itemView.findViewById(R.id.locationChip)
        val salaryChip: Chip = itemView.findViewById(R.id.salaryChip)
        val timestampChip: Chip = itemView.findViewById(R.id.timestampChip)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(itemView)
    }

    // taking timestamp as an input and converting it to the string based on hours and days
    fun getTimeDifference(timestamp: Long?): String {
        timestamp ?: return ""

        val now = System.currentTimeMillis()
        val diffInMillis = now - timestamp
        val diffInHours = diffInMillis / (1000 * 60 * 60)

        return when {
            diffInHours < 24 -> "$diffInHours h ago"
            diffInHours < 24 * 7 -> "${diffInHours / 24} d ago"
            else -> "7+"
        }
    }
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = posts[position]
        holder.titleTextView.text = currentPost.title
        holder.descriptionTextView.text = currentPost.description
        holder.locationChip.text = currentPost.location
        holder.salaryChip.text = currentPost.salary
        holder.timestampChip.text = getTimeDifference(currentPost.timestamp)
        holder.authorTextView.text = "Posted by ${currentPost.author}"
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
