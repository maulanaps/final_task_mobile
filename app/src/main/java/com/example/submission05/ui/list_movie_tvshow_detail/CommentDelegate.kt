package com.example.submission05.ui.list_movie_tvshow_detail

import com.example.submission05.data.model.CommentEntity

interface CommentDelegate {
    fun onItemClicked(commentEntity: CommentEntity)
}