package com.akshaykalola.newsapp.repository

import com.akshaykalola.newsapp.api.RetrofitInstance
import com.akshaykalola.newsapp.db.ArticleDatabase
import com.akshaykalola.newsapp.models.Article

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getTopHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.newsApi.getTopHeadlines(countryCode, pageNumber)

    suspend fun getSearchNews(query: String, pageNumber: Int) =
        RetrofitInstance.newsApi.searchNews(query, pageNumber)

    suspend fun upsertArticle(article: Article) = db.getArticleDao().updateOrInsert(article)

    fun getAllArticles() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}