package com.example.submission03.movie

import com.example.submission03.model.Movie

interface MovieDelegate {
    fun onItemClicked(movie: Movie)
}