package com.teksiak.nutrilight.search.data

fun interface SearchResultListener {
    fun onSearchResult(resultCount: Int)
}