package com.akshaykalola.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshaykalola.newsapp.R
import com.akshaykalola.newsapp.adapters.ArticleAdapter
import com.akshaykalola.newsapp.helpers.Resource
import com.akshaykalola.newsapp.ui.NewsViewModel
import com.akshaykalola.newsapp.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    companion object {
        private const val TAG = "BreakingNewsFragment"
    }

    lateinit var viewModel: NewsViewModel
    lateinit var articleAdapter: ArticleAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.topHeadlines.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> showProgressBar()
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { data ->
                        articleAdapter.articles.submitList(data.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG, "onViewCreated: Error: $it")
                    }
                }
            }
        })
    }

    private fun showProgressBar() {
        paginationProgressBar?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        paginationProgressBar?.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter()
        rvBreakingNews.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }
    }
}