package dev.gumil.places.domain

import dev.gumil.places.data.PlacesApi
import dev.gumil.places.data.PlacesType
import dev.gumil.places.data.ResponseException
import dev.gumil.places.db.PlaceDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class PlacesRepositoryImplTest {

    private val placesApi: PlacesApi = FakePlacesApi()
    private val mockDao = mockk<PlaceDao>(relaxUnitFun = true)
    private val placesRepository = PlacesRepositoryImpl(placesApi, mockDao)

    @Test(expected = ResponseException::class)
    fun `test request nearby should throw exception`() = runBlocking {
        val nearby = placesRepository.getNearby(0.0, 0.0, PlacesType.RESTAURANT)
    }

    @Test
    fun `test request nearby should return places`() = runBlocking {
        // 1. Setup
        val places = listOf(
            Place(
                "test_api_bar_1",
                52.3760864,
                4.880024100000001,
                "Willemine Semeins Jazz vocalist en coach",
                false,
                0.0,
                "Egelantiersstraat 121-B, Amsterdam"
            ),
            Place(
                "test_api_bar_2",
                52.375233,
                4.880817,
                "Café De Nieuwe Lelie",
                true,
                4.6,
                "Nieuwe Leliestraat 83, Amsterdam"
            )
        )

        val expected = "testToken" to places

        // 2. Action
        val actual = placesRepository.getNearby(0.0, 0.0, PlacesType.BAR)

        // 3. Verification
        assertEquals(expected, actual)
        coVerify(exactly = 1) { mockDao.insertAll(places) }
        confirmVerified(mockDao)
    }

    @Test
    fun `test request nearby with null page token should return places`() = runBlocking {
        // 1. Setup
        val places = listOf(
            Place(
                "test_api_cafe",
                52.3760864,
                4.880024100000001,
                "Willemine Semeins Jazz vocalist en coach",
                false,
                0.0,
                "Egelantiersstraat 121-B, Amsterdam"
            )
        )

        val expected = null to places

        // 2. Action
        val actual = placesRepository.getNearby(0.0, 0.0, PlacesType.CAFE)

        // 3. Verification
        assertEquals(expected, actual)
        coVerify(exactly = 1) { mockDao.insertAll(places) }
        confirmVerified(mockDao)
    }

    @Test
    fun `test request nearby IOException should get fro database`() = runBlocking {
        // 1. Setup
        val places = listOf(
            Place(
                "from_database",
                52.3760864,
                4.880024100000001,
                "Willemine Semeins Jazz vocalist en coach",
                false,
                0.0,
                "Egelantiersstraat 121-B, Amsterdam"
            )
        )

        val expected = null to places

        coEvery { mockDao.getAll() } returns places

        // 2. Action
        val actual = placesRepository.getNearby(0.0, 0.0, PlacesType.CAFE, "")

        // 3. Verification
        assertEquals(expected, actual)
        coVerify(exactly = 0) { mockDao.insertAll(places) }
        coVerify(exactly = 1) { mockDao.getAll() }
        confirmVerified(mockDao)
    }
}
