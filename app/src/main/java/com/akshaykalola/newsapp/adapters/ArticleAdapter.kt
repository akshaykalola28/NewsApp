package com.akshaykalola.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshaykalola.newsapp.R
import com.akshaykalola.newsapp.models.Article

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ArticleVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleVH {
        return ArticleVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        )
    }

    override fun getItemCount(): Int = articles.currentList.size

    override fun onBindViewHolder(holder: ArticleVH, position: Int) {
        val article = articles.currentList[position]
        holder.itemView.apply {

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem.url == newItem.url

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
            oldItem == newItem

    }

    val articles = AsyncListDiffer(this, differCallback)

    inner class ArticleVH(itemView: View) : RecyclerView.ViewHolder(itemView)
}