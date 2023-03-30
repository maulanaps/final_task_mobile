package com.example.submission05.ui.list_movie

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.submission03.R

class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val poster: ImageView = itemView.findViewById(R.id.ivPoster)
    val title: TextView = itemView.findViewById(R.id.tvTitle)
    val rating: TextView = itemView.findViewById(R.id.tvRating)
    val overview: TextView = itemView.findViewById(R.id.tvOverview)
}