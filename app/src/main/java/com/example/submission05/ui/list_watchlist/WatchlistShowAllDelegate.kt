package com.example.submission05.ui.list_watchlist

import com.example.submission03.model.MovieAndTvShow

interface WatchlistShowAllDelegate {
    fun onItemClicked(movieAndTvShow: MovieAndTvShow)
    fun onDeleteBtnClicked(movieAndTvShow: MovieAndTvShow)
}