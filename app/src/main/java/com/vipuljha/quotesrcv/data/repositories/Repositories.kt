package com.vipuljha.quotesrcv.data.repositories

import com.vipuljha.quotesrcv.data.remote.RetrofitProvider

class Repositories {
    private val api = RetrofitProvider.api

    suspend fun getQuotes() = api.getQuotes()
}