package com.akshaykalola.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshaykalola.newsapp.R
import com.akshaykalola.newsapp.adapters.ArticleAdapter
import com.akshaykalola.newsapp.helpers.Constants
import com.akshaykalola.newsapp.helpers.Resource
import com.akshaykalola.newsapp.ui.NewsViewModel
import com.akshaykalola.newsapp.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {

    companion object {
        private const val TAG = "SearchFragment"
    }

    lateinit var viewModel: NewsViewModel
    private val articleAdapter: ArticleAdapter = ArticleAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        var job: Job? = null
        etSearch.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                delay(Constants.SEARCH_NEWS_TIME_DELAY)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.getSearchNews(it.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> showProgressBar()
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        articleAdapter.articles.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.d(TAG, "onViewCreated: $it")
                    }
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar?.visibility = View.GONE
    }

    private fun showProgressBar() {
        paginationProgressBar?.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        rvSearchNews.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }

        articleAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                Bundle().apply { putSerializable("article", it) }
            )
        }
    }
}