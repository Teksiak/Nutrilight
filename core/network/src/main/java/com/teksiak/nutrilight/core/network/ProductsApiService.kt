package com.teksiak.nutrilight.core.network

import com.teksiak.nutrilight.core.network.dto.RemoteProductDto
import com.teksiak.nutrilight.core.network.dto.SearchResultDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {

    @GET("api/v2/product/{barcode}")
    suspend fun getProduct(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String = "code,product_name,brands,nova_group,quantity,ecoscore_score,nutriscore_score,packaging,nutrients,ingredients,nutriments,allergens,selected_images"
    ): Response<RemoteProductDto>

    @GET("cgi/search.pl")
    suspend fun searchProducts(
        @Query("search_terms") searchTerms: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
    ): Response<SearchResultDto>

    @GET(NetworkConstants.WORLD_BASE_URL + "cgi/search.pl")
    suspend fun searchProductsGlobally(
        @Query("search_terms") searchTerms: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
    ): Response<SearchResultDto>

}