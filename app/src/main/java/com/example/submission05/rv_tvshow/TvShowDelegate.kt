package com.example.submission05.rv_tvshow

import com.example.submission05.model.TvShow

interface TvShowDelegate {
    fun onItemClicked(tvShow: TvShow)
}