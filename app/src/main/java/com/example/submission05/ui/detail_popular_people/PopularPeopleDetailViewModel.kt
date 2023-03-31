package com.example.submission05.ui.detail_popular_people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission05.data.remote.api.PopularPeoplesApi
import com.example.submission05.data.remote.api.RetrofitHelper
import com.example.submission05.data.remote.model.PopularPeopleDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularPeopleDetailViewModel : ViewModel() {
    private var _peopleDetail = MutableLiveData<PopularPeopleDetail>()
    val peopleDetail: LiveData<PopularPeopleDetail>
        get() = _peopleDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean>
        get() = _isError

    fun peopleDetailApiCall(peopleId: Int) {
        // INITIATE VALUE
        _isLoading.value = true
        _isError.value = false

        val peopleDetailApi = RetrofitHelper.getInstance().create(PopularPeoplesApi::class.java)
        val call = peopleDetailApi.getPeopleDetail(peopleId)

        call.enqueue(object : Callback<PopularPeopleDetail> {
            override fun onResponse(
                call: Call<PopularPeopleDetail>,
                response: Response<PopularPeopleDetail>
            ) {
                if (response.isSuccessful) {
                    val peopleDetail = response.body()!!
                    _peopleDetail.value = peopleDetail
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<PopularPeopleDetail>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}