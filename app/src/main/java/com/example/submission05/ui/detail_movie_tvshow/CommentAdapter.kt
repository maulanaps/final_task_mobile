package com.example.submission05.ui.detail_movie_tvshow

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.submission03.R
import com.example.submission05.data.entity.CommentEntity

class CommentAdapter : RecyclerView.Adapter<CommentViewHolder>() {

    private val list: MutableList<CommentEntity> = mutableListOf()
    var delegate: CommentDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        // input data into view holder
        holder.writer.text = list[position].writer
        holder.content.text = list[position].content

        // events
        holder.itemView.setOnClickListener {
            delegate?.onItemClicked(list[position])
        }
    }

    override fun getItemCount(): Int {
        Log.d("blah", "getItemCount: $list.size")
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(comments: List<CommentEntity>) {
        list.clear()
        list.addAll(comments)
        Log.d("blah", "setAdapter: ${comments.size}")
        notifyDataSetChanged()
    }
}