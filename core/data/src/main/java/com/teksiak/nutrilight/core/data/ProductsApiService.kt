package com.teksiak.nutrilight.core.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApiService {

    @GET("product/{barcode}")
    suspend fun getProduct(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String = "code,product_name,brands,nova_group,quantity,ecoscore_score,nutriscore_score,packaging,nutrients,ingredients,nutriments,allergens,selected_images"
    ): Response<RemoteProductDto>

}