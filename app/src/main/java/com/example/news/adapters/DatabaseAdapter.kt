package com.example.news.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news.databinding.ItemsNewsBinding
import com.example.news.models.Article

class DatabaseAdapter : RecyclerView.Adapter<DatabaseAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemsNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article) {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(article)
            }
            binding.tvTitle.text = article.title
            binding.tvSource.text = article.source?.name ?: ""
            binding.tvDescription.text = article.description
            binding.tvPublishedAt.text = article.publishedAt
            Glide.with(itemView).load(article.urlToImage).into(binding.ivArticleImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    var onItemClickListener: ((Article) -> Unit)? = null

}