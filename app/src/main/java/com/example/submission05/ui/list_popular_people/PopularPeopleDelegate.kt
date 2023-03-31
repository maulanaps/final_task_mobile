package com.example.submission05.ui.list_popular_people

import com.example.submission05.data.remote.model.PopularPeople

interface PopularPeopleDelegate {
    fun onItemClicked(popularPeople: PopularPeople)
}