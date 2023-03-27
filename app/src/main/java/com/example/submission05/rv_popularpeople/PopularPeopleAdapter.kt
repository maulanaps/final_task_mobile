package com.example.submission03.movie

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission03.R
import com.example.submission03.model.Movie
import com.example.submission05.model.PopularPeople
import com.example.submission05.model.PopularPeoples

class PopularPeopleAdapter : RecyclerView.Adapter<PopularPeopleViewHolder>() {

    private val list: MutableList<PopularPeople> = mutableListOf()
    var delegate: PopularPeopleDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularPeopleViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.popular_people_item, parent, false)
        return PopularPeopleViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PopularPeopleViewHolder, position: Int) {
        // input data into view holder
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original" + list[position].profilePath)
            .placeholder(R.drawable.image_placeholder)
            .into(holder.profilePic)
        Log.d("foo", "onBindViewHolder: $list[position]")

        holder.name.text = list[position].name
        holder.itemView.setOnClickListener {
            delegate?.onItemClicked(list[position])
        }
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(popularPeoples: List<PopularPeople>) {
        list.clear()
        list.addAll(popularPeoples)
        Log.d("bar", "setAdapter: ${popularPeoples.size}")
        notifyDataSetChanged()
    }
}