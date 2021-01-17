package com.aurelien.test.services

import com.aurelien.test.services.models.PlacesApiResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesApi {

    @GET("/v1/coverage/fr-idf/places")
    suspend fun getPlaces(@Query("q") search: String): Response<PlacesApiResult>
}
