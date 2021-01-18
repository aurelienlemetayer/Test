package com.aurelien.test.data.repositories

import com.aurelien.test.core.services.ApiCoroutinesClient
import com.aurelien.test.data.local.PlacesDao
import com.aurelien.test.data.remote.PlacesApi
import com.aurelien.test.data.models.Place
import com.aurelien.test.data.models.PlacesApiResult
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val placesDao: PlacesDao,
    private val apiCoroutinesClient: ApiCoroutinesClient,
    private val placesApi: PlacesApi
) {

    companion object {
        private const val TAG = "PlacesRepository"
    }

    suspend fun getPlaces(search: String): ApiCoroutinesClient.Result<List<Place>> {
        return apiCoroutinesClient.call(
            //I have not to implement pagination, but feel free to tell me if you want :)
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

    suspend fun getFavoritePlaces(): List<Place> {
        return placesDao.getAllPlaces()
    }


    suspend fun insertFavoritePlace(place: Place) {
        placesDao.insert(place)
    }

    suspend fun deleteFavoritePlace(placeId: String) {
        placesDao.delete(placeId)
    }
}