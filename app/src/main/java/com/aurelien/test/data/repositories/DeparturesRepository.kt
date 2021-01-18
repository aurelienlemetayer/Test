package com.aurelien.test.data.repositories

import com.aurelien.test.core.services.ApiCoroutinesClient
import com.aurelien.test.data.models.CommercialMode
import com.aurelien.test.data.models.Departure
import com.aurelien.test.data.models.DepartureApiResult
import com.aurelien.test.data.models.DeparturesApiResult
import com.aurelien.test.data.remote.DeparturesApi
import java.util.*
import javax.inject.Inject

class DeparturesRepository @Inject constructor(
    private val apiCoroutinesClient: ApiCoroutinesClient,
    private val departuresApi: DeparturesApi
) {

    companion object {
        private const val TAG = "DeparturesRepository"
    }

    suspend fun getDepartures(placeId: String): ApiCoroutinesClient.Result<List<Departure>> {
        return apiCoroutinesClient.call(
            //I have not to implement pagination, but feel free to tell me if you want :)
            { departuresApi.getDepartures(placeId) },
            TAG,
            "Error getting departures"
        ).asResultDepartures()
    }

    private fun ApiCoroutinesClient.Result<DeparturesApiResult>.asResultDepartures(): ApiCoroutinesClient.Result<List<Departure>> {
        return when (this) {
            is ApiCoroutinesClient.Result.Success -> ApiCoroutinesClient.Result.Success(this.data.asDepartures())
            is ApiCoroutinesClient.Result.Error -> this
        }
    }

    private fun DeparturesApiResult.asDepartures(): List<Departure> {
        return departures.map { it.asDeparture() }
            .filter { it.commercialMode != CommercialMode.UNKNOWN }
    }

    private fun DepartureApiResult.asDeparture(): Departure {
        val commercialMode = when (displayInformation.commercialMode) {
            CommercialMode.RER.value -> CommercialMode.RER
            CommercialMode.METRO.value -> CommercialMode.METRO
            CommercialMode.BUS.value -> CommercialMode.BUS
            //I chose to just manage RER, METRO, BUS to simplify :)
            else -> CommercialMode.UNKNOWN
        }

        return Departure(
            displayInformation.code,
            displayInformation.color,
            stopDateTime.departureDateTime,
            commercialMode,
            displayInformation.direction
        )
    }
}