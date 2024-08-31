package com.teksiak.nutrilight.core.data

import com.teksiak.nutrilight.core.domain.remote.RemoteProductDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {

    @GET(Constants.BASE_URL + "/product/{barcode}")
    suspend fun getProduct(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String = ""
    ): Response<RemoteProductDto>

}