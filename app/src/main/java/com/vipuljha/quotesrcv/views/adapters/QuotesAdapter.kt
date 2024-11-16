package com.vipuljha.quotesrcv.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vipuljha.quotesrcv.databinding.ItemQuotesListBinding
import com.vipuljha.quotesrcv.models.Quote

class QuotesAdapter : ListAdapter<Quote, QuotesAdapter.QuotesViewHolder>(QuotesDiffCallback()) {
    private var onItemClickListener: ((Quote) -> Unit)? = null

    inner class QuotesViewHolder(private val binding: ItemQuotesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote) {
            with(binding) {
                tvQuote.text = quote.quote
                tvAuthorName.text = quote.author
                root.setOnClickListener {
                    onItemClickListener?.invoke(quote)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuotesViewHolder {
        val binding = ItemQuotesListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuotesViewHolder, position: Int) {
        val quote = getItem(position)
        holder.bind(quote)
    }

    fun setOnItemClickListener(listener: (Quote) -> Unit) {
        onItemClickListener = listener
    }
}

class QuotesDiffCallback : DiffUtil.ItemCallback<Quote>() {
    override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
        return oldItem == newItem
    }
}