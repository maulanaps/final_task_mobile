package com.example.submission05.ui.list_popular_people

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.submission03.R

class PopularPeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val profilePic: ImageView = itemView.findViewById(R.id.ivPeopleProfile)
    val name: TextView = itemView.findViewById(R.id.tvPeopleName)
}