@file:OptIn(ExperimentalCoroutinesApi::class)

package com.teksiak.nutrilight.core.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.teksiak.nutrilight.core.domain.ProductsRepository
import com.teksiak.nutrilight.core.domain.product.NovaGroup
import com.teksiak.nutrilight.core.domain.product.Nutriments
import com.teksiak.nutrilight.core.domain.product.Product
import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import retrofit2.Response

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductsRepositoryTest {

    private lateinit var productsRepository: ProductsRepository
    private lateinit var apiService: ProductsApiService

    @BeforeAll
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterAll
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ParameterizedTest
    @MethodSource("successResponses")
    fun `test getProduct with success apiResponse`(apiResponse: Response<RemoteProductDto>, expectedProduct: Product) = runTest {
        apiService = object : ProductsApiService {
            override suspend fun getProduct(
                barcode: String,
                fields: String
            ): Response<RemoteProductDto> {
                return apiResponse
            }
        }
        productsRepository = ProductsRepositoryImpl(apiService)

        val result = productsRepository.scanProduct("20724696")

        assertThat(result).isInstanceOf(Result.Success::class)
        val resultProduct = (result as Result.Success).data

        assertThat(resultProduct).isEqualTo(expectedProduct)
    }

    @ParameterizedTest
    @MethodSource("errorResponses")
    fun `test getProduct with error apiResponse`(errorCode: Int, expectedError: DataError.Remote) = runTest {
        apiService = object : ProductsApiService {
            override suspend fun getProduct(
                barcode: String,
                fields: String
            ): Response<RemoteProductDto> {
                return Response.error(errorCode, "".toResponseBody())
            }
        }
        productsRepository = ProductsRepositoryImpl(apiService)

        val result = productsRepository.scanProduct("20724696")
        assertThat(result).isInstanceOf(Result.Error::class)
        val error = (result as Result.Error).error

        assertThat(error).isEqualTo(expectedError)
    }

    @Test
    fun `test getProduct when product is not found`() = runTest {
        apiService = object : ProductsApiService {
            override suspend fun getProduct(
                barcode: String,
                fields: String
            ): Response<RemoteProductDto> {
                return Response.success(
                    RemoteProductDto(
                        status = 0,
                        code = "20724696",
                        product = RemoteProduct(
                            productName = null,
                            brands = null,
                            quantity = null,
                            packaging = null,
                            novaGroup = null,
                            nutriments = null,
                            allergens = null,
                            ingredients = null,
                            ecoscoreScore = null,
                            nutriscoreScore = null
                        )
                    )
                )
            }
        }
        productsRepository = ProductsRepositoryImpl(apiService)

        val result = productsRepository.scanProduct("20724696")
        assertThat(result).isInstanceOf(Result.Error::class)
        val error = (result as Result.Error).error

        assertThat(error).isEqualTo(DataError.Remote.PRODUCT_NOT_FOUND)
    }

    companion object {
        @JvmStatic
        fun errorResponses() =  listOf(
            Arguments.of(
                408, DataError.Remote.REQUEST_TIMEOUT
            ),
            Arguments.of(
                429, DataError.Remote.TOO_MANY_REQUESTS
            ),
            Arguments.of(
                500, DataError.Remote.SERVER_ERROR
            ),
        )

        @JvmStatic
        fun successResponses() =  listOf(
            Arguments.of(
                Response.success(
                    RemoteProductDto(
                        status = 1,
                        code = "20724696",
                        product = RemoteProduct(
                            productName = "Californian Almond test",
                            brands = "Alesto,Lidl,Solent",
                            quantity = "200g",
                            packaging = "en:Andere Kunststoffe,en:Kunststoff,en:Tüte",
                            novaGroup = 1,
                            nutriments = RemoteNutriments(
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
                                RemoteIngredient(
                                    id = "en:almond",
                                    text = "almonds"
                                )
                            ),
                            ecoscoreScore = 24,
                            nutriscoreScore = -3
                        )
                    )
                ),
                Product(
                    code = "20724696",
                    name = "Californian Almond test",
                    brands = "Alesto,Lidl,Solent",
                    quantity = "200g",
                    packaging = "Andere Kunststoffe, Kunststoff, Tüte",
                    novaGroup = NovaGroup.NOVA_1,
                    nutriments = Nutriments(
                        energyKj = 2567f,
                        energyKcal = 621f,
                        fat = 53.3f,
                        saturatedFat = 4.3f,
                        carbohydrates = 4.8f,
                        sugars = 4.8f,
                        fiber = 12.1f,
                        protein = 24.5f,
                        salt = 0.01f
                    ),
                    allergens = "nuts",
                    ingredients = listOf(
                        "almonds"
                    ),
                    score = 4.6f
                )
            ),
            Arguments.of(
                Response.success(
                    RemoteProductDto(
                        status = 1,
                        code = "20724696",
                        product = RemoteProduct(
                            productName = "Californian Almond test",
                            brands = "Alesto,Lidl,Solent",
                            quantity = "200g",
                            packaging = "en:Andere Kunststoffe,en:Kunststoff,en:Tüte",
                            novaGroup = null,
                            nutriments = null,
                            allergens = "en:nuts",
                            ingredients = null,
                            ecoscoreScore = 24,
                            nutriscoreScore = -3
                        )
                    )
                ),
                Product(
                    code = "20724696",
                    name = "Californian Almond test",
                    brands = "Alesto,Lidl,Solent",
                    quantity = "200g",
                    packaging = "Andere Kunststoffe, Kunststoff, Tüte",
                    novaGroup = null,
                    nutriments = null,
                    allergens = "nuts",
                    ingredients = listOf(),
                    score = 4.6f
                )
            ),
            Arguments.of(
                Response.success(
                    RemoteProductDto(
                        status = 1,
                        code = "20724696",
                        product = RemoteProduct(
                            productName = "Californian Almond test",
                            brands = "Alesto,Lidl,Solent",
                            quantity = null,
                            packaging = "en:Andere Kunststoffe,en:Kunststoff,en:Tüte",
                            novaGroup = null,
                            nutriments = null,
                            allergens = null,
                            ingredients = null,
                            ecoscoreScore = null,
                            nutriscoreScore = -3
                        )
                    )
                ),
                Product(
                    code = "20724696",
                    name = "Californian Almond test",
                    brands = "Alesto,Lidl,Solent",
                    quantity = null,
                    packaging = "Andere Kunststoffe, Kunststoff, Tüte",
                    novaGroup = null,
                    nutriments = null,
                    allergens = null,
                    ingredients = listOf(),
                    score = null
                )
            )
        )
    }
}