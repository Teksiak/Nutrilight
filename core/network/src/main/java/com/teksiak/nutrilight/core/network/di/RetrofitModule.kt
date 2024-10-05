package com.teksiak.nutrilight.core.network.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.teksiak.nutrilight.core.domain.Country
import com.teksiak.nutrilight.core.domain.SettingsRepository
import com.teksiak.nutrilight.core.network.NetworkConstants
import com.teksiak.nutrilight.core.network.ProductsApiService
import com.teksiak.nutrilight.core.network.interceptor.BaseUrlInterceptor
import com.teksiak.nutrilight.core.network.interceptor.UserAgentInterceptor
import com.teksiak.nutrilight.core.network.util.toBaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        settingsRepository: SettingsRepository,
        applicationScope: CoroutineScope
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(UserAgentInterceptor(context))
        .addInterceptor(
            BaseUrlInterceptor(
                settingsRepository = settingsRepository,
                applicationScope = applicationScope
            )
        )
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.HEADERS
            }
        )
        .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(Country.POLAND.toBaseUrl())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideProductsApi(retrofit: Retrofit): ProductsApiService = retrofit.create(
        ProductsApiService::class.java)
}