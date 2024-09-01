@file:OptIn(ExperimentalCoroutinesApi::class)

package com.teksiak.nutrilight.core.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Nutriments
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.Result
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
class ProductsRepositoryTest {

    private lateinit var apiService: ProductsApiService
    private lateinit var mockWebServer: MockWebServer
    private lateinit var productsRepository: ProductsRepository

    @BeforeAll
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            start()
        }

        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ProductsApiService::class.java)

        productsRepository = ProductsRepositoryImpl(apiService)

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterAll
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
    }

    @Test
    fun `test getProduct with complete product`() = runTest {
        val mockResponse = File("src/test/resources/product.json").readText()
        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        val result = productsRepository.getProduct("20724696")

        assertThat(result).isInstanceOf(Result.Success::class)

        val product = (result as Result.Success).data
        val expectedProduct = Product(
            code = "20724696",
            name = "Californian Almond test",
            brands = "Alesto,Lidl,Solent",
            quantity = "200g",
            packaging = "en:Andere Kunststoffe,en:Kunststoff,en:Tüte",
            novaGroup = NovaGroup.NOVA_1,
            nutriments = Nutriments(
                energyKj = 2567,
                energyKcal = 621,
                fat = 53.3f,
                saturatedFat = 4.3f,
                carbohydrates = 4.8f,
                sugars = 4.8f,
                fiber = 12.1f,
                protein = 24.5f,
                salt = 0.01f
            ),
            allergens = listOf(
                "Nuts"
            ),
            ingredients = listOf(
                "almonds"
            ),
            score = 4.6f
        )

        assertThat(product).isEqualTo(expectedProduct)

    }
}