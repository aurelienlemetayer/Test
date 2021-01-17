package com.aurelien.test.data.models

import com.squareup.moshi.Json
import java.util.*

class DeparturesApiResult(val departures: List<DepartureApiResult>)

class DepartureApiResult(
    @Json(name = "display_informations") val displayInformation: DisplayInformation,
    @Json(name = "stop_date_time") val stopDateTime: StopDateTime
)

class DisplayInformation(
    val code: String,
    val color: String,
    @Json(name = "commercial_mode") val commercialMode: String,
    val direction: String
)

class StopDateTime(@Json(name = "departure_date_time") val departureDateTime: Date)
