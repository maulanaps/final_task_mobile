package com.example.submission05.rv_watchlist_showAll

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission03.R
import com.example.submission03.model.Movie

class WatchlistShowAllAdapter : RecyclerView.Adapter<WatchlistShowAllViewHolder>() {

    private val list: MutableList<Movie> = mutableListOf()
    var delegate: WatchlistShowAllDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistShowAllViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.watchlist_all_item, parent, false)
        return WatchlistShowAllViewHolder(view)
    }

    override fun onBindViewHolder(holder: WatchlistShowAllViewHolder, position: Int) {
        // input data into view holder
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original" + list[position].posterPath)
            .placeholder(R.drawable.image_placeholder)
            .into(holder.poster)

        holder.title.text = list[position].originalTitle

        holder.deleteBtn.setOnClickListener {
            delegate?.onDeleteBtnClicked(list[position])
        }

        holder.itemView.setOnClickListener {
            delegate?.onItemClicked(list[position])
        }
    }

    override fun getItemCount(): Int {
        Log.d("blah", "getItemCount: $list.size")
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(movies: List<Movie>) {
        list.clear()
        list.addAll(movies)
        Log.d("blah", "setAdapter: ${movies.size}")
        notifyDataSetChanged()
    }
}