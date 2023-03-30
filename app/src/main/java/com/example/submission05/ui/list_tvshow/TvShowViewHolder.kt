package com.example.submission05.ui.list_tvshow

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.submission03.R

class TvShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val poster: ImageView = itemView.findViewById(R.id.ivPosterTv)
    val title: TextView = itemView.findViewById(R.id.tvNameTv)
    val rating: TextView = itemView.findViewById(R.id.tvRatingTv)
    val overview: TextView = itemView.findViewById(R.id.tvOverviewTv)
}