package com.example.submission05.ui.main

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.submission03.R

class WatchlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val poster: ImageView = itemView.findViewById(R.id.ivPoster)
    val title: TextView = itemView.findViewById(R.id.tvTitle)
}