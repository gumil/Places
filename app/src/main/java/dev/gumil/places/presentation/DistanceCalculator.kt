package dev.gumil.places.presentation

import android.location.Location
import dev.gumil.places.domain.Place

internal interface DistanceCalculator {

    fun sortByDistance(latitude: Double, longitude: Double, places: List<Place>): List<Place>
}

internal class DistanceCalculatorImpl : DistanceCalculator {

    override fun sortByDistance(
        latitude: Double,
        longitude: Double,
        places: List<Place>
    ): List<Place> {
        return places
            .map {
                val sourceLocation = Location("source").apply {
                    this.latitude = latitude
                    this.longitude = longitude
                }

                val destinationLocation = Location("destination").apply {
                    this.latitude = it.latitude
                    this.longitude = it.longitude
                }

                it.copy(distance = sourceLocation.distanceTo(destinationLocation))
            }
            .sortedBy { it.distance }
    }
}
