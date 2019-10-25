package dev.gumil.places.data

import dev.gumil.places.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

internal interface PlacesApi {

    @GET("/maps/api/place/nearbysearch/json?key=${BuildConfig.API_KEY}&rankby=distance")
    suspend fun getNearby(
        @Query("location") location: String,
        @Query("type") type: String
    ): ApiResult
}
