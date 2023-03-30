package com.example.submission05.ui.list_popular_people

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission03.model.MovieAndTvShow
import com.example.submission05.api.PopularPeoplesApi
import com.example.submission05.api.RetrofitHelper
import com.example.submission05.data.model.PopularPeople
import com.example.submission05.data.model.PopularPeoples
import com.example.submission05.utils.dialog.ErrorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularPeopleViewModel : ViewModel() {
    private val _popularPeopleList = MutableLiveData<List<PopularPeople>>()
    val popularPeopleList: LiveData<List<PopularPeople>>
        get() = _popularPeopleList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError

    fun popularPeopleApiCall() {
        _isLoading.value = true
        _isError.value = false

        val popularPeoplesApi = RetrofitHelper.getInstance().create(PopularPeoplesApi::class.java)
        val call: Call<PopularPeoples> = popularPeoplesApi.getPopularPeoples()

        call.enqueue(object : Callback<PopularPeoples> {
            override fun onResponse(call: Call<PopularPeoples>, response: Response<PopularPeoples>) {
                if (response.isSuccessful) {
                    val list = response.body()?.results
                    list?.let {
                        Log.d("foo", "onResponse: ${list.size}")
                        _popularPeopleList.value = it
                        _isLoading.value = false
                    }
                }
            }

            override fun onFailure(call: Call<PopularPeoples>, t: Throwable) {
                Log.d("foo", "onFailure: ${t.message}")
                _isLoading.value = false
                _isError.value = true
                }

        })
    }
}