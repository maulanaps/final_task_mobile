package com.example.submission05.ui.list_tvshow

import com.example.submission05.data.remote.model.TvShow

interface TvShowDelegate {
    fun onItemClicked(tvShow: TvShow)
}