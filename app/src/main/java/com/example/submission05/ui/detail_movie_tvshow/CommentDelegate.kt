package com.example.submission05.ui.detail_movie_tvshow

import com.example.submission05.data.local.entity.CommentEntity

interface CommentDelegate {
    fun onItemClicked(commentEntity: CommentEntity)
}