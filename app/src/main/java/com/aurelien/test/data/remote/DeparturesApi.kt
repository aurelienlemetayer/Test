package com.aurelien.test.data.remote

import com.aurelien.test.data.models.DeparturesApiResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DeparturesApi {

    @GET("/v1/coverage/fr-idf/stop_areas/{placeId}/departures?")
    suspend fun getDepartures(@Path("placeId") placeId: String): Response<DeparturesApiResult>
}

