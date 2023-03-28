package com.example.submission03.movie

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission03.R
import com.example.submission03.model.Movie

class MovieAdapter : RecyclerView.Adapter<MovieViewHolder>() {

    private val list: MutableList<Movie> = mutableListOf()
    var delegate: MovieDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        // input data into view holder
        Glide.with(holder.itemView.context)
            .load("https://image.tmdb.org/t/p/original" + list[position].posterPath)
            .placeholder(R.drawable.image_placeholder)
            .into(holder.poster)
        val item = list[position]
//        Log.d("foo", "onBindViewHolder: $item")

        holder.title.text = list[position].title
        holder.rating.text = "Rating: " + list[position].voteAverage.toString()
        holder.overview.text = list[position].overview
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