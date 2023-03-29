package com.example.submission05.rv_watchlist

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission03.R
import com.example.submission03.model.MovieAndTvShow

class WatchlistAdapter : RecyclerView.Adapter<WatchlistViewHolder>() {

    private val list: MutableList<MovieAndTvShow> = mutableListOf()
    var delegate: WatchlistDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.watchlist_item, parent, false)
        return WatchlistViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {

        // input data into view holder
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original" + list[position].posterPath)
            .placeholder(R.drawable.image_placeholder)
            .into(holder.poster)
        holder.title.text = list[position].originalTitle

        // on click
        holder.itemView.setOnClickListener {
            delegate?.onItemClicked(list[position])
        }
    }

    override fun getItemCount(): Int {
        Log.d("blah", "getItemCount: $list.size")
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(movieAndTvShows: List<MovieAndTvShow>) {
        list.clear()
        list.addAll(movieAndTvShows)
        Log.d("blah", "setAdapter: ${movieAndTvShows.size}")
        notifyDataSetChanged()
    }
}