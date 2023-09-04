package com.hamza.newsapp.adapters;

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hamza.newsapp.databinding.ItemArticlePreviewBinding
import com.hamza.newsapp.models.Article


class NewsAdapter : RecyclerView.Adapter<NewsAdapter.Holder>() {
    inner class Holder constructor(val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                // onItemClick?.onItemClick(list?.get(layoutPosition)?.id!!)
            }
        }
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView.context).load(data.urlToImage).into(ivArticleImage)
            tvSource.text = data.source.name
            tvDescription.text = data.description
            tvTitle.text = data.title
            tvPublishedAt.text = data.publishedAt
            setOnItemClickListener {
                onItemClickListener?.let {
                    it(data)
                }
            }
        }
    }


    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener: ((Article) -> Unit)) {
        onItemClickListener = listener
    }

}