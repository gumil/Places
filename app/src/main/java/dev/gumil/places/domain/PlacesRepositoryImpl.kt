package dev.gumil.places.domain

import dev.gumil.places.data.PlacesApi
import dev.gumil.places.data.PlacesType
import dev.gumil.places.data.RESPONSE_SUCCESS
import dev.gumil.places.data.ResponseException

internal class PlacesRepositoryImpl(
    private val placesApi: PlacesApi
) : PlacesRepository {

    override suspend fun getNearby(
        latitude: Double,
        longitude: Double,
        type: PlacesType,
        pageToken: String
    ): Places {
        val response = placesApi.getNearby("$latitude,$longitude", type.toString(), pageToken)

        if (response.status != RESPONSE_SUCCESS) throw ResponseException()

        return response.nextPageToken to response.results.map {
            Place(
                it.geometry.location.lat,
                it.geometry.location.lng,
                it.name,
                it.openingHours.openNow,
                it.rating,
                it.vicinity
            )
        }
    }
}
