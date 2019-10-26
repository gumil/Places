package dev.gumil.places.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


enum class ResponseCode(val code: String) {
    SUCCESS("OK"),
    INVALID("INVALID_REQUEST")
}

@JsonClass(generateAdapter = true)
internal data class ApiResponse(
    @Json(name = "next_page_token")
    val nextPageToken: String?,
    val results: List<PlaceResponse>,
    val status: String
)

@JsonClass(generateAdapter = true)
internal data class PlaceResponse(
    val id: String,
    val geometry: GeometryResponse,
    val name: String,
    @Json(name = "opening_hours")
    val openingHours: OpeningHoursResponse = OpeningHoursResponse(false),
    val rating: Double = 0.0,
    val vicinity: String
)

internal data class GeometryResponse(
    val location: LocationResponse
)

internal data class LocationResponse(
    val lat: Double,
    val lng: Double
)

@JsonClass(generateAdapter = true)
internal data class OpeningHoursResponse(
    @Json(name = "open_now")
    val openNow: Boolean
)
