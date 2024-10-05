@file:OptIn(ExperimentalCoroutinesApi::class)

package com.teksiak.nutrilight.core.data

import assertk.assertThat
import assertk.assertions.isEqualTo
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
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductsApiServiceTest {

    private lateinit var gson: Gson
    private lateinit var apiService: com.teksiak.nutrilight.core.network.ProductsApiService
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
            .create(com.teksiak.nutrilight.core.network.ProductsApiService::class.java)

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterAll
    fun tearDown() {
        mockWebServer.shutdown()
        Dispatchers.resetMain()
    }

    @ParameterizedTest
    @MethodSource("apiResponses")
    fun `test if product is parsed correctly`(apiResponse: String, expectedParsing: com.teksiak.nutrilight.core.network.RemoteProductDto) = runTest {
        val mockResponse = File(apiResponse).readText()
        mockWebServer.url("/product/20724696")
        mockWebServer.enqueue(MockResponse().setBody(mockResponse))

        val productDto = apiService.getProduct("20724696").body()

        assertThat(productDto).isEqualTo(expectedParsing)
    }

    companion object {
        @JvmStatic
        fun apiResponses() = listOf(
            Arguments.of(
                "src/test/resources/product_complete.json",
                com.teksiak.nutrilight.core.network.RemoteProductDto(
                    status = 1,
                    code = "20724696",
                    product = com.teksiak.nutrilight.core.network.RemoteProduct(
                        productName = "Californian Almond test",
                        brands = "Alesto,Lidl,Solent",
                        quantity = "200g",
                        packaging = "en:Andere Kunststoffe,en:Kunststoff,en:Tüte",
                        novaGroup = 1,
                        nutriments = com.teksiak.nutrilight.core.network.RemoteNutriments(
                            energyKj = 2567f,
                            energyKcal = 621f,
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
                            com.teksiak.nutrilight.core.network.RemoteIngredient(
                                id = "en:almond",
                                text = "almonds"
                            )
                        ),
                        ecoscoreScore = 24,
                        nutriscoreScore = -3
                    )
                ),
            ),
            Arguments.of(
                "src/test/resources/product_no_brands_packaging.json",
                com.teksiak.nutrilight.core.network.RemoteProductDto(
                    status = 1,
                    code = "20724696",
                    product = com.teksiak.nutrilight.core.network.RemoteProduct(
                        productName = "Californian Almond test",
                        brands = null,
                        quantity = "200g",
                        packaging = null,
                        novaGroup = 1,
                        nutriments = com.teksiak.nutrilight.core.network.RemoteNutriments(
                            energyKj = 2567f,
                            energyKcal = 621f,
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
                            com.teksiak.nutrilight.core.network.RemoteIngredient(
                                id = "en:almond",
                                text = "almonds"
                            )
                        ),
                        ecoscoreScore = 24,
                        nutriscoreScore = -3
                    )
                ),
            ),
            Arguments.of(
                "src/test/resources/product_no_ingredients_allergens.json",
                com.teksiak.nutrilight.core.network.RemoteProductDto(
                    status = 1,
                    code = "20724696",
                    product = com.teksiak.nutrilight.core.network.RemoteProduct(
                        productName = "Californian Almond test",
                        brands = "Alesto,Lidl,Solent",
                        quantity = "200g",
                        packaging = "en:Andere Kunststoffe,en:Kunststoff,en:Tüte",
                        novaGroup = 1,
                        nutriments = com.teksiak.nutrilight.core.network.RemoteNutriments(
                            energyKj = 2567f,
                            energyKcal = 621f,
                            fat100g = 53.3f,
                            saturatedFat100g = 4.3f,
                            carbohydrates100g = 4.8f,
                            sugars100g = 4.8f,
                            fiber100g = 12.1f,
                            proteins100g = 24.5f,
                            salt100g = 0.01f
                        ),
                        allergens = null,
                        ingredients = null,
                        ecoscoreScore = 24,
                        nutriscoreScore = -3
                    )
                )
            ),
            Arguments.of(
                "src/test/resources/product_no_nutriments.json",
                com.teksiak.nutrilight.core.network.RemoteProductDto(
                    status = 1,
                    code = "20724696",
                    product = com.teksiak.nutrilight.core.network.RemoteProduct(
                        productName = "Californian Almond test",
                        brands = "Alesto,Lidl,Solent",
                        quantity = "200g",
                        packaging = "en:Andere Kunststoffe,en:Kunststoff,en:Tüte",
                        novaGroup = 1,
                        nutriments = null,
                        allergens = "en:nuts",
                        ingredients = listOf(
                            com.teksiak.nutrilight.core.network.RemoteIngredient(
                                id = "en:almond",
                                text = "almonds"
                            )
                        ),
                        ecoscoreScore = null,
                        nutriscoreScore = null
                    )
                ),
            ),
            Arguments.of(
                "src/test/resources/product_no_nutriments_nova.json",
                com.teksiak.nutrilight.core.network.RemoteProductDto(
                    status = 1,
                    code = "20724696",
                    product = com.teksiak.nutrilight.core.network.RemoteProduct(
                        productName = "Californian Almond test",
                        brands = "Alesto,Lidl,Solent",
                        quantity = "200g",
                        packaging = "en:Andere Kunststoffe,en:Kunststoff,en:Tüte",
                        novaGroup = null,
                        nutriments = null,
                        allergens = "en:nuts",
                        ingredients = listOf(
                            com.teksiak.nutrilight.core.network.RemoteIngredient(
                                id = "en:almond",
                                text = "almonds"
                            )
                        ),
                        ecoscoreScore = null,
                        nutriscoreScore = null
                    )
                ),
            ),
            Arguments.of(
                "src/test/resources/no_product_found.json",
                com.teksiak.nutrilight.core.network.RemoteProductDto(
                    status = 0,
                    code = "20724696",
                    product = null
                ),
            ),
        )
    }

}