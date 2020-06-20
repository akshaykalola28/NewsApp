package com.akshaykalola.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshaykalola.newsapp.helpers.Resource
import com.akshaykalola.newsapp.models.Article
import com.akshaykalola.newsapp.models.NewsResponse
import com.akshaykalola.newsapp.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    val topHeadlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var topHeadlinePage: Int = 1
    var topHeadlinesResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage: Int = 1

    init {
        getTopHeadline("in")
    }

    fun getTopHeadline(countryCode: String) = viewModelScope.launch {
        topHeadlines.postValue(Resource.Loading())
        val response = newsRepository.getTopHeadlines(countryCode, topHeadlinePage)
        topHeadlines.postValue(handelTopHeadlines(response))
    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.getSearchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNews(response))
    }

    private fun handelTopHeadlines(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { res ->
                topHeadlinePage++
                if (topHeadlinesResponse == null) {
                    topHeadlinesResponse = res
                } else {
                    topHeadlinesResponse?.articles?.addAll(res.articles)
                }
                return Resource.Success(topHeadlinesResponse ?: res)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNews(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getSavedArticles() = newsRepository.getAllArticles()
}