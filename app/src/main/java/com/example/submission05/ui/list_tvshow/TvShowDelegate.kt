package com.example.submission05.ui.list_tvshow

import com.example.submission05.data.model.TvShow

interface TvShowDelegate {
    fun onItemClicked(tvShow: TvShow)
}