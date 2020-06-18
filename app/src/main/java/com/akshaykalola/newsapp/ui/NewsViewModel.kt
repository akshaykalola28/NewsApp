package com.akshaykalola.newsapp.ui

import androidx.lifecycle.ViewModel
import com.akshaykalola.newsapp.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

}