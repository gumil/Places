package dev.gumil.places.data

import com.squareup.moshi.Moshi
import dev.gumil.places.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


internal object ApiFactory {

    private const val BASE_URL = "https://maps.googleapis.com/"
    private const val TIMEOUT = 10L

    fun create(url: String = BASE_URL) =
        createApi(url, createOkHttpClient(createLoggingInterceptor()))

    private fun createApi(url: String, okHttpClient: OkHttpClient): PlacesApi =
        Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(createConverter())
            .build()
            .create(PlacesApi::class.java)

    private fun createOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()

    private fun createLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

    private fun createConverter(): Converter.Factory =
        MoshiConverterFactory.create(Moshi.Builder().build())
}
