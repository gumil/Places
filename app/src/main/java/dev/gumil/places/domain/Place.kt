package dev.gumil.places.domain

internal data class Place(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val isOpen: Boolean,
    val rating: Double,
    val vicinity: String,
    val distance: Float = 0f
)

internal typealias Places = Pair<String?, List<Place>>
