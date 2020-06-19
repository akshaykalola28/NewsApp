package com.akshaykalola.newsapp.repository

import com.akshaykalola.newsapp.api.RetrofitInstance
import com.akshaykalola.newsapp.db.ArticleDatabase

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getTopHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.newsApi.getTopHeadlines(countryCode, pageNumber)

    suspend fun getSearchNews(query: String, pageNumber: Int) =
        RetrofitInstance.newsApi.searchNews(query, pageNumber)
}