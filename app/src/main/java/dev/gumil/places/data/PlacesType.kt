package dev.gumil.places.data

import java.util.*

enum class PlacesType {
    CAFE,
    BAR,
    RESTAURANT;

    override fun toString(): String {
        return name.toLowerCase(Locale.ROOT)
    }
}
