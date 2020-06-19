package com.akshaykalola.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.akshaykalola.newsapp.R
import com.akshaykalola.newsapp.adapters.ArticleAdapter
import com.akshaykalola.newsapp.ui.NewsViewModel
import com.akshaykalola.newsapp.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    companion object {
        private const val TAG = "SavedNewsFragment"
    }

    lateinit var viewModel: NewsViewModel
    private val articleAdapter: ArticleAdapter = ArticleAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
    }

    private fun hideProgressBar() {
        paginationProgressBar?.visibility = View.GONE
    }

    private fun showProgressBar() {
        paginationProgressBar?.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        rvSavedNews.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = articleAdapter
        }

        articleAdapter.setOnItemClickListener {
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                Bundle().apply { putSerializable("article", it) }
            )
        }
    }
}