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

        assertThat(productDto).isNotNull()
        assertThat(productDto?.status).isEqualTo(1)
        assertThat(productDto?.code).isEqualTo("20724696")

        val remoteProduct = productDto?.product
        remoteProduct?.let { product ->
            assertThat(product.productName).isEqualTo("Californian Almond test")
            assertThat(product.brands).isEqualTo("Alesto,Lidl,Solent")
            assertThat(product.quantity).isEqualTo("200g")
            assertThat(product.packaging).isEqualTo("en:Andere Kunststoffe,en:Kunststoff,en:Tüte")
            assertThat(product.novaGroup).isEqualTo(1)
            assertThat(product.nutriments.energyKj).isEqualTo(2567)
            assertThat(product.nutriments.energyKcal).isEqualTo(621)
            assertThat(product.nutriments.fat100g).isEqualTo(53.3f)
            assertThat(product.nutriments.saturatedFat100g).isEqualTo(4.3f)
            assertThat(product.nutriments.carbohydrates100g).isEqualTo(4.8f)
            assertThat(product.nutriments.sugars100g).isEqualTo(4.8f)
            assertThat(product.nutriments.fiber100g).isEqualTo(12.1f)
            assertThat(product.nutriments.proteins100g).isEqualTo(24.5f)
            assertThat(product.nutriments.salt100g).isEqualTo(0.01f)
            assertThat(product.ecoscoreScore).isEqualTo(24)
            assertThat(product.nutriscoreScore).isEqualTo(-3)
            assertThat(product.allergens).isEqualTo("en:nuts")
            assertThat(product.ingredients).isEqualTo(
                listOf(
                    RemoteIngredient(
                        id = "en:almond",
                        text = "almonds"
                    )
                )
            )
        }

    }

}