package dev.gumil.places.presentation

import dev.gumil.places.data.PlacesType
import dev.gumil.places.data.ResponseException
import dev.gumil.places.domain.Place
import dev.gumil.places.domain.Places
import dev.gumil.places.domain.PlacesRepository

internal class FakePlacesRepository : PlacesRepository {
    override suspend fun getNearby(
        latitude: Double,
        longitude: Double,
        type: PlacesType,
        pageToken: String?
    ): Places {
        return when (type) {
            PlacesType.BAR -> {
                val places = listOf(
                    Place(
                        52.3760864,
                        4.880024100000001,
                        "Willemine Semeins Jazz vocalist en coach",
                        false,
                        0.0,
                        "Egelantiersstraat 121-B, Amsterdam"
                    ),
                    Place(
                        52.375233,
                        4.880817,
                        "Café De Nieuwe Lelie",
                        true,
                        4.6,
                        "Nieuwe Leliestraat 83, Amsterdam"
                    )
                )

                "testToken" to places
            }
            PlacesType.CAFE -> {
                val places = listOf(
                    Place(
                        52.3760864,
                        4.880024100000001,
                        "Willemine Semeins Jazz vocalist en coach",
                        false,
                        0.0,
                        "Egelantiersstraat 121-B, Amsterdam"
                    )
                )

                null to places
            }
            else -> {
                throw ResponseException()
            }
        }
    }
}
