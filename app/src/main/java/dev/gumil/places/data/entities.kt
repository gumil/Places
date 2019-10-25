package dev.gumil.places.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ApiResult(
    @Json(name = "next_page_token")
    val nextPageToken: String?,
    val results: List<PlaceEntity>
)

@JsonClass(generateAdapter = true)
internal data class PlaceEntity(
    val id: String,
    val geometry: GeometryEntity,
    val name: String,
    @Json(name = "opening_hours")
    val openingHours: OpeningHoursEntity = OpeningHoursEntity(false),
    val rating: Double = 0.0,
    val vicinity: String
)

internal data class GeometryEntity(
    val location: LocationEntity
)

internal data class LocationEntity(
    val lat: Double,
    val lng: Double
)

@JsonClass(generateAdapter = true)
internal data class OpeningHoursEntity(
    @Json(name = "open_now")
    val openNow: Boolean
)
