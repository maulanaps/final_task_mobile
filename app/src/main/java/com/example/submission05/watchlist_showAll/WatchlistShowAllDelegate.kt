package com.example.submission05.watchlist_showAll

import com.example.submission03.model.Movie

interface WatchlistShowAllDelegate {
    fun onItemClicked(movie: Movie)
    fun onDeleteBtnClicked(movie: Movie)
}