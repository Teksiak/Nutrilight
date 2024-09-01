package com.teksiak.nutrilight.core.data.util

import com.teksiak.nutrilight.core.domain.util.DataError
import com.teksiak.nutrilight.core.domain.util.Result
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> safeApiCall(call: () -> Response<T>): Result<T, DataError.Remote> {
    val response = try {
        call()
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Error(DataError.Remote.NO_INTERNET)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(DataError.Remote.SERIALIZATION_ERROR)
    } catch (e: Exception) {
        if(e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(DataError.Remote.UNKNOWN_ERROR)
    }

    return responseToResult(response)
}

fun <T> responseToResult(response: Response<T>): Result<T, DataError.Remote> {
    return when(response.code()) {
        in 200..299 -> Result.Success(response.body()!!)
        408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Remote.SERVER_ERROR)
        else -> Result.Error(DataError.Remote.UNKNOWN_ERROR)
    }
}