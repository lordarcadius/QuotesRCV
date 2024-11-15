package com.vipuljha.quotesrcv.data.remote

import com.vipuljha.quotesrcv.models.QuotesModel
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {
    @GET("quotes")
    suspend fun getQuotes() : Response<QuotesModel>
}