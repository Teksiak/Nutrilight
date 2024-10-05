package com.teksiak.nutrilight.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultDto(
    val count: Int,
    val page: Int,
    val pageCount: Int,
    val pageSize: Int,
    val products: List<RemoteProductDto>,
    val skip: Int
)