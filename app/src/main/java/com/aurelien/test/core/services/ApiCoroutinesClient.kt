package com.aurelien.test.core.services

import com.aurelien.test.core.logger.Logger
import com.aurelien.test.core.services.models.ApiCallException
import com.aurelien.test.core.services.models.NullResponseBodyException
import okhttp3.Request
import okio.Buffer
import retrofit2.Response
import java.io.IOException

class ApiCoroutinesClient {

    suspend fun <T> call(
        apiCall: suspend () -> Response<T>,
        moduleName: String,
        errorMessage: String
    ): Result<T> {
        return try {
            val response = apiCall.invoke()
            val body = response.body()
            if (response.isSuccessful) {
                body?.let {
                    Result.Success(it)
                } ?: run {
                    val result =
                        Result.Error(NullResponseBodyException("The response body is null"))
                    logError(moduleName, result, errorMessage)
                    result
                }
            } else {
                val result = Result.Error(response.getApiCallException())
                logError(moduleName, result, errorMessage)
                result
            }
        } catch (e: Exception) {
            val result = Result.Error(e)
            logError(moduleName, result, errorMessage)
            result
        }
    }

    private fun <T> Response<T>.getApiCallException(): ApiCallException {
        return ApiCallException(
            "${code()} - ${message()}",
            raw().request.method,
            raw().request.url.toString(),
            bodyToString(raw().request)
        )
    }

    private fun logError(
        moduleName: String,
        result: Result.Error,
        errorMessage: String
    ) {
        Logger.log(
            Logger.Level.ERROR,
            moduleName,
            "$errorMessage - ${result.error.message}",
        )
    }

    private fun bodyToString(request: Request): String? {
        return try {
            val copy: Request = request.newBuilder().build()
            val buffer = Buffer()
            copy.body?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            null
        }
    }

    sealed class Result<out T> {
        class Success<out T>(val data: T) : Result<T>()
        class Error(val error: Throwable) : Result<Nothing>()
    }
}
