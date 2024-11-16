package com.vipuljha.quotesrcv.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vipuljha.quotesrcv.data.repositories.Repositories

class QuotesViewModelProviderFactory(
    private val repositories: Repositories
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(QuotesViewModel::class.java) -> {
                QuotesViewModel(repositories) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
