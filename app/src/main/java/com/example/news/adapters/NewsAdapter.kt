package com.example.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.databinding.ItemsNewsBinding
import com.example.news.models.Article

class NewsAdapter : PagingDataAdapter<Article, NewsAdapter.ViewHolder>(DIFFER_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) holder.bind(currentItem)
    }

    inner class ViewHolder(private val binding: ItemsNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            Glide.with(itemView).load(article.urlToImage).into(binding.ivArticleImage)
            binding.tvTitle.text = article.title
            binding.tvSource.text = article.source?.name ?: ""
            binding.tvDescription.text = article.description
            binding.tvPublishedAt.text = article.publishedAt
            itemView.setOnClickListener { onItemClickListener?.invoke(article) }
        }
    }

    companion object {
         val DIFFER_CALLBACK = object : DiffUtil.ItemCallback<Article>() {

            override fun areItemsTheSame(
                oldItem: Article,
                newItem: Article
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Article,
                newItem: Article
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    // onClick listener lambda
    var onItemClickListener: ((Article) -> Unit)? = null

}