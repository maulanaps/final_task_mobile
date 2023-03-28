package com.example.submission05.rv_comment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.submission03.R

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val writer: TextView = itemView.findViewById(R.id.tvCommentWriter)
    val content: TextView = itemView.findViewById(R.id.tvCommentContent)
}