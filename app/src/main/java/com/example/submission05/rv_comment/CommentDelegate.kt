package com.example.submission05.rv_comment

import com.example.submission05.model.CommentEntity

interface CommentDelegate {
    fun onItemClicked(commentEntity: CommentEntity)
}