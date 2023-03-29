package com.example.submission05.rv_watchlist_showAll

import com.example.submission03.model.MovieAndTvShow

interface WatchlistShowAllDelegate {
    fun onItemClicked(movieAndTvShow: MovieAndTvShow)
    fun onDeleteBtnClicked(movieAndTvShow: MovieAndTvShow)
}