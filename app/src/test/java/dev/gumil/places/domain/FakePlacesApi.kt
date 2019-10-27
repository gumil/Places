package dev.gumil.places.domain

import dev.gumil.places.data.*

internal class FakePlacesApi: PlacesApi {
    override suspend fun getNearby(location: String, type: String, token: String?): ApiResponse {
        return when (type) {
            PlacesType.BAR.toString() -> {
                ApiResponse(
                    "testToken",
                    listOf(
                        PlaceResponse(
                            "test_api_bar_1",
                            GeometryResponse(LocationResponse(52.3760864, 4.880024100000001)),
                            "Willemine Semeins Jazz vocalist en coach",
                            OpeningHoursResponse(false),
                            0.0,
                            "Egelantiersstraat 121-B, Amsterdam"
                        ),
                        PlaceResponse(
                            "test_api_bar_2",
                            GeometryResponse(LocationResponse(52.375233, 4.880817)),
                            "Café De Nieuwe Lelie",
                            OpeningHoursResponse(true),
                            4.6,
                            "Nieuwe Leliestraat 83, Amsterdam"

                        )
                    ),
                    ResponseCode.SUCCESS.code
                )
            }
            PlacesType.CAFE.toString() -> {
                ApiResponse(
                    null,
                    listOf(
                        PlaceResponse(
                            "test_api_cafe",
                            GeometryResponse(LocationResponse(52.3760864, 4.880024100000001)),
                            "Willemine Semeins Jazz vocalist en coach",
                            OpeningHoursResponse(false),
                            0.0,
                            "Egelantiersstraat 121-B, Amsterdam"
                        )
                    ),
                    ResponseCode.SUCCESS.code
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
