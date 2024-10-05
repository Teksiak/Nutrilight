package com.teksiak.nutrilight.core.network.interceptor

import com.teksiak.nutrilight.core.domain.Country
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.network.util.toBaseUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlInterceptor(
    private val settingsRepository: SettingsRepository,
    private val applicationScope: CoroutineScope,
): Interceptor {
    private val baseUrl = MutableStateFlow(Country.POLAND.toBaseUrl())

    init {
        settingsRepository.countryCode
            .onEach { country ->
                baseUrl.value = country.toBaseUrl()
            }
            .launchIn(applicationScope)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder().apply {
            url(
                request.url.newBuilder()
                    .host(baseUrl.value.toHttpUrl().host)
                    .build()
            )
        }.build()

        return chain.proceed(builder)
    }
}