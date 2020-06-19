package com.akshaykalola.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshaykalola.newsapp.R
import com.akshaykalola.newsapp.adapters.ArticleAdapter
import com.akshaykalola.newsapp.ui.NewsViewModel
import com.akshaykalola.newsapp.ui.activities.MainActivity
import com.google.android.material.snackbar.Snackbar
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

        viewModel.getSavedArticles().observe(viewLifecycleOwner, Observer {
            articleAdapter.articles.submitList(it)
        })
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

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = articleAdapter.articles.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view!!, "Successfully Deleted.", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rvSavedNews)
    }
}