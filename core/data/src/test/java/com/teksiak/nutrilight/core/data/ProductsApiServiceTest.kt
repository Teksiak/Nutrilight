@file:OptIn(ExperimentalCoroutinesApi::class)

package com.teksiak.nutrilight.core.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductsApiServiceTest {

    private lateinit var gson: Gson
    private lateinit var apiService: ProductsApiService
    private lateinit var mockWebServer: MockWebServer

    @BeforeAll
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            start()
        }

        gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ProductsApiService::class.java)

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterAll
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
    }

    @Test
    fun `test if product is parsed correctly`() = runTest {
        val mockResponse = File("src/test/resources/product.json").readText()
        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        val productDto = apiService.getProduct("20724696").body()

        val expectedProductDto = RemoteProductDto(
            status = 1,
            code = "20724696",
            product = RemoteProduct(
                productName = "Californian Almond test",
                brands = "Alesto,Lidl,Solent",
                quantity = "200g",
                packaging = "en:Andere Kunststoffe,en:Kunststoff,en:Tüte",
                novaGroup = 1,
                nutriments = RemoteNutriments(
                    energyKj = 2567,
                    energyKcal = 621,
                    fat100g = 53.3f,
                    saturatedFat100g = 4.3f,
                    carbohydrates100g = 4.8f,
                    sugars100g = 4.8f,
                    fiber100g = 12.1f,
                    proteins100g = 24.5f,
                    salt100g = 0.01f
                ),
                allergens = "en:nuts",
                ingredients = listOf(
                    RemoteIngredient(
                        id = "en:almond",
                        text = "almonds"
                    )
                ),
                ecoscoreScore = 24,
                nutriscoreScore = -3
            )
        )

        assertThat(productDto).isEqualTo(expectedProductDto)

    }

}