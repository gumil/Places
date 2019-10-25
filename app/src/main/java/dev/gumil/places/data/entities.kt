package dev.gumil.places.data

import com.squareup.moshi.Json

internal data class ApiResult(
    @Json(name = "next_page_token")
    val nextPageToken: String,
    val results: List<PlaceEntity>
)

internal data class PlaceEntity(
    val geometry: GeometryEntity,
    val name: String,
    @Json(name = "opening_hours")
    val openingHours: OpeningHoursEntity,
    @Json(name = "place_id")
    val placeId: String,
    val rating: Double,
    val vicinity: String
)

internal data class GeometryEntity(
    val location: LocationEntity
)

internal data class LocationEntity(
    val lat: Double,
    val lng: Double
)

internal data class OpeningHoursEntity(
    @Json(name = "open_now")
    val openNow: Boolean
)
