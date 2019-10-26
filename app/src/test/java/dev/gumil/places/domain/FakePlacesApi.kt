package dev.gumil.places.domain

import dev.gumil.places.data.*

internal class FakePlacesApi: PlacesApi {
    override suspend fun getNearby(location: String, type: String, token: String): ApiResponse {
        return when (type) {
            PlacesType.BAR.toString() -> {
                ApiResponse(
                    "testToken",
                    listOf(
                        PlaceResponse(
                            "e730d0cb50adb87001819ead2701303e0aef1258",
                            GeometryResponse(LocationResponse(52.3760864, 4.880024100000001)),
                            "Willemine Semeins Jazz vocalist en coach",
                            OpeningHoursResponse(false),
                            0.0,
                            "Egelantiersstraat 121-B, Amsterdam"
                        ),
                        PlaceResponse(
                            "a0d53630e2182845a24ec0f74c0bce46b7716b27",
                            GeometryResponse(LocationResponse(52.375233, 4.880817)),
                            "Café De Nieuwe Lelie",
                            OpeningHoursResponse(true),
                            4.6,
                            "Nieuwe Leliestraat 83, Amsterdam"

                        )
                    ),
                    RESPONSE_SUCCESS
                )
            }
            PlacesType.CAFE.toString() -> {
                ApiResponse(
                    null,
                    listOf(
                        PlaceResponse(
                            "e730d0cb50adb87001819ead2701303e0aef1258",
                            GeometryResponse(LocationResponse(52.3760864, 4.880024100000001)),
                            "Willemine Semeins Jazz vocalist en coach",
                            OpeningHoursResponse(false),
                            0.0,
                            "Egelantiersstraat 121-B, Amsterdam"
                        )
                    ),
                    RESPONSE_SUCCESS
                )
            }
            else -> {
                ApiResponse(
                    null,
                    emptyList(),
                    "error"
                )
            }
        }
    }
}
