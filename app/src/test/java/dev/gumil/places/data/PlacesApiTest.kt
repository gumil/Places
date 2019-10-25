package dev.gumil.places.data

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream

internal class PlacesApiTest {

    private val mockServer = MockWebServer().apply { start() }

    private val placesApi = ApiFactory.create(mockServer.url("/").toString())

    @Test
    fun getNearby() = runBlocking {
        // 1. Setup
        mockServer.enqueue(createMockResponse(readFromFile("nearby.json")))
        val expected = ApiResponse(
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
            )
        )

        // 2. Action
        val apiResult = placesApi.getNearby("52.3760508,4.8788894", "bar")

        // 3. Verification
        assertEquals(expected, apiResult)
    }

    @Test
    fun testParsing() = runBlocking {
        // 1. Setup
        mockServer.enqueue(createMockResponse(readFromFile("complete.json")))

        // 2. Action
        val apiResult = placesApi.getNearby("52.3760508,4.8788894", "bar")

        // 3. Verification
        assertEquals(20, apiResult.results.size)
    }

    private fun readFromFile(file: String): String {
        val inputStream = this.javaClass.classLoader!!.getResourceAsStream(file)
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length = inputStream.read(buffer)
        while (length != -1) {
            result.write(buffer, 0, length)
            length = inputStream.read(buffer)
        }
        return result.toString("UTF-8")
    }

    private fun createMockResponse(body: String): MockResponse {
        return MockResponse().apply {
            setResponseCode(200)
            setBody(body)
            addHeader("Content-type: application/json")
        }
    }
}
