package com.akshaykalola.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.akshaykalola.newsapp.R
import com.akshaykalola.newsapp.ui.NewsViewModel
import com.akshaykalola.newsapp.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val article = args.article

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        fabSaveArticle.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article Saved.", Snackbar.LENGTH_LONG).show()
        }
    }
}