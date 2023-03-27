package com.example.submission03.movie

import com.example.submission03.model.Movie
import com.example.submission05.model.PopularPeople

interface PopularPeopleDelegate {
    fun onItemClicked(popularPeople: PopularPeople)
}