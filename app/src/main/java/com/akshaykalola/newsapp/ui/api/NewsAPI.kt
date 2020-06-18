package com.akshaykalola.newsapp.ui.api

import com.akshaykalola.newsapp.ui.helpers.Constants
import com.akshaykalola.newsapp.ui.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") countryCode: String = "in",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = Constants.NEWS_API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = Constants.NEWS_API_KEY
    ): Response<NewsResponse>
}