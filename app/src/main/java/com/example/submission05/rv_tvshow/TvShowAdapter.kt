package com.example.submission05.rv_tvshow

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission03.R
import com.example.submission03.movie.TvShowViewHolder
import com.example.submission05.model.TvShow

class TvShowAdapter : RecyclerView.Adapter<TvShowViewHolder>() {

    private val list: MutableList<TvShow> = mutableListOf()
    var delegate: TvShowDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tvshow_item, parent, false)
        return TvShowViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        // input data into view holder
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original" + list[position].posterPath)
            .placeholder(R.drawable.image_placeholder)
            .into(holder.poster)
        val item = list[position]
        Log.d("blah", "onBindViewHolder: $item")

        holder.title.text = list[position].name
        holder.rating.text = "Rating: " + list[position].voteAverage.toString()
        holder.overview.text = list[position].overview
        holder.itemView.setOnClickListener {
            delegate?.onItemClicked(list[position])
        }
    }

    override fun getItemCount(): Int {
        Log.d("blah", "getItemCount: ${list.size}")
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(tvShows: List<TvShow>) {
        Log.d("blah", "setAdapter: ${tvShows.size}")
        list.clear()
        list.addAll(tvShows)
        notifyDataSetChanged()
    }
}