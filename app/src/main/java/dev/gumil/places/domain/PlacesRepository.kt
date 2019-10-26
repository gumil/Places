package dev.gumil.places.domain

import dev.gumil.places.data.PlacesType

internal interface PlacesRepository {

    suspend fun getNearby(
        latitude: Double,
        longitude: Double,
        type: PlacesType,
        pageToken: String? = null
    ) : Places
}
