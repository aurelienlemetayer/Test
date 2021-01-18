package com.aurelien.test.core.services.models

class NullResponseBodyException(message: String) : Throwable(message)

class ApiCallException(
    message: String,
    val requestHttpMethod: String,
    val requestUrl: String,
    val requestBody: String?
) : Throwable(message)