package com.akshaykalola.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshaykalola.newsapp.R
import com.akshaykalola.newsapp.adapters.ArticleAdapter
import com.akshaykalola.newsapp.helpers.Resource
import com.akshaykalola.newsapp.ui.NewsViewModel
import com.akshaykalola.newsapp.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_top_headline.*

class TopHeadlineFragment : Fragment(R.layout.fragment_top_headline) {

    companion object {
        private const val TAG = "TopHeadlineFragment"
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

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1)) {
                viewModel.getTopHeadline("in")
            }
        }
    }

    private fun setupRecyclerView() {
        articleAdapter = ArticleAdapter()
        rvBreakingNews.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
            addOnScrollListener(scrollListener)
        }

        articleAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
    }
}