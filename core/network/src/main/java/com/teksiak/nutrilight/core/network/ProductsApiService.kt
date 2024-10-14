package com.teksiak.nutrilight.core.network

import com.teksiak.nutrilight.core.network.dto.ProductResultDto
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
    ): Response<ProductResultDto>

    @GET("cgi/search.pl")
    suspend fun searchProducts(
        @Query("search_terms") searchTerms: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = SEARCH_PAGE_SIZE,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1,
    ): Response<SearchResultDto>

    @GET(WORLD_BASE_URL + "cgi/search.pl")
    suspend fun searchProductsGlobally(
        @Query("search_terms") searchTerms: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = SEARCH_PAGE_SIZE,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1,
    ): Response<SearchResultDto>

    companion object {
        const val WORLD_BASE_URL = "https://world.openfoodfacts.net/"
        const val POLAND_BASE_URL = "https://pl.openfoodfacts.net/"
        const val FRANCE_BASE_URL = "https://fr.openfoodfacts.net/"
        const val GERMANY_BASE_URL = "https://de.openfoodfacts.net/"
        const val ITALY_BASE_URL = "https://it.openfoodfacts.net/"
        const val SPAIN_BASE_URL = "https://es.openfoodfacts.net/"
        const val UNITED_KINGDOM_BASE_URL = "https://uk.openfoodfacts.net/"

        const val SEARCH_PAGE_SIZE = 25
    }

}