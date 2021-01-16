package com.aurelien.test.services

import com.aurelien.test.core.services.ApiCoroutinesClient
import com.aurelien.test.services.models.Place
import com.aurelien.test.services.models.PlacesApiResult
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val apiCoroutinesClient: ApiCoroutinesClient,
    private val placesApi: PlacesApi
) {

    companion object {
        private const val TAG = "PlacesRepository"
    }

    suspend fun getPlaces(search: String): ApiCoroutinesClient.Result<List<Place>> {
        return apiCoroutinesClient.call(
            { placesApi.getPlaces(search) },
            TAG,
            "Error getting places"
        ).asResultPlaces()
    }

    private fun ApiCoroutinesClient.Result<PlacesApiResult>.asResultPlaces(): ApiCoroutinesClient.Result<List<Place>> {
        return when (this) {
            is ApiCoroutinesClient.Result.Success -> ApiCoroutinesClient.Result.Success(this.data.places)
            is ApiCoroutinesClient.Result.Error -> this
        }
    }
}