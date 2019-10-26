package dev.gumil.places.domain

internal data class Place(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val isOpen: Boolean,
    val rating: Double,
    val vicinity: String
)

internal typealias Places = Pair<String?, List<Place>>
