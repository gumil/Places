package dev.gumil.places.domain

import dev.gumil.places.data.PlacesApi
import dev.gumil.places.data.PlacesType
import dev.gumil.places.data.ResponseCode
import dev.gumil.places.data.ResponseException
import dev.gumil.places.db.PlaceDao
import kotlinx.coroutines.delay
import java.io.IOException

internal class PlacesRepositoryImpl(
    private val placesApi: PlacesApi,
    private val placeDao: PlaceDao
) : PlacesRepository {

    override suspend fun getNearby(
        latitude: Double,
        longitude: Double,
        type: PlacesType,
        pageToken: String?
    ): Places {
        try {
            val response = placesApi.getNearby("$latitude,$longitude", type.toString(), pageToken)

            when (response.status) {
                ResponseCode.INVALID.code -> {
                    // The request can fail when requests come in succession
                    // Retry after 0.5 second
                    delay(500L)
                    return getNearby(latitude, longitude, type, pageToken)
                }
                ResponseCode.SUCCESS.code -> {
                    // do nothing
                }
                else -> throw ResponseException()
            }
            val list = response.results.map {
                Place(
                    it.id,
                    it.geometry.location.lat,
                    it.geometry.location.lng,
                    it.name,
                    it.openingHours.openNow,
                    it.rating,
                    it.vicinity
                )
            }
            placeDao.insertAll(list)
            return response.nextPageToken to list
        } catch (e: IOException) {
            return null to placeDao.getAll()
        }
    }
}
