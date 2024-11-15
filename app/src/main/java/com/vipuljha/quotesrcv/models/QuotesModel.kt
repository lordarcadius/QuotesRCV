package com.vipuljha.quotesrcv.models

data class QuotesModel(
    val limit: Int,
    val quotes: List<Quote>,
    val skip: Int,
    val total: Int
)