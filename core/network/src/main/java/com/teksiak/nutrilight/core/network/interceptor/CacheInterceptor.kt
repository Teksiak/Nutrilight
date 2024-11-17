package com.teksiak.nutrilight.core.network.interceptor

import android.content.Context
import com.teksiak.nutrilight.core.network.ProductsApiService
import com.teksiak.nutrilight.core.network.util.hasNetwork
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor(
    private val context: Context
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = if(hasNetwork(context)) {
            request.newBuilder()
                .header("Cache-Control", "public, max-age=${ProductsApiService.CACHE_MAX_AGE}")
                .build()
        } else {
            request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=${ProductsApiService.CACHE_MAX_STALE}")
                .build()
        }
        return chain.proceed(newRequest)
    }
}