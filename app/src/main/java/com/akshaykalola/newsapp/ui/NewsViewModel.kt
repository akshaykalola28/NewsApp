package com.akshaykalola.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshaykalola.newsapp.helpers.Resource
import com.akshaykalola.newsapp.models.NewsResponse
import com.akshaykalola.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    val topHeadlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val topHeadlinePage: Int = 1

    init {
        getTopHeadline("in")
    }

    fun getTopHeadline(countryCode: String) = viewModelScope.launch {
        topHeadlines.postValue(Resource.Loading())
        val response = newsRepository.getTopHeadlines(countryCode, topHeadlinePage)
        topHeadlines.postValue(handelTopHeadlines(response))
    }

    private fun handelTopHeadlines(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}