package com.vipuljha.quotesrcv.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipuljha.quotesrcv.data.repositories.Repositories
import com.vipuljha.quotesrcv.models.QuotesModel
import com.vipuljha.quotesrcv.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class QuotesViewModel(private val repository: Repositories) : ViewModel() {

    private val _quotes = MutableStateFlow<Resource<QuotesModel>>(Resource.Loading())
    val quotes: StateFlow<Resource<QuotesModel>> = _quotes


    fun getQuotes() = viewModelScope.launch {
        _quotes.value = Resource.Loading()
        try {
            val response = repository.getQuotes()
            _quotes.value = handleResponse(response) // Assign result back to _quotes
        } catch (e: Exception) {
            _quotes.value = Resource.Error(message = e.message)
        }
    }

    private fun handleResponse(response: Response<QuotesModel>): Resource<QuotesModel> {
        return if (response.isSuccessful) {
            response.body()?.let {
                Resource.Success(it)
            } ?: Resource.Error(message = "Response body is null")
        } else {
            Resource.Error(message = response.message())
        }
    }
}