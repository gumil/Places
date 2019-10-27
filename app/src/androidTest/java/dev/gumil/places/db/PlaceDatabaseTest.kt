package dev.gumil.places.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.gumil.places.domain.Place
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PlaceDatabaseTest {

    private lateinit var db: PlaceDatabase
    private lateinit var placeDao: PlaceDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PlaceDatabase::class.java
        ).build()
        placeDao = db.placeDao()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndGetAll() = runBlocking {
        // 1. Setup
        val place1 = Place(
            "test_db_bar_1",
            52.3760864,
            4.880024100000001,
            "Willemine Semeins Jazz vocalist en coach",
            false,
            0.0,
            "Egelantiersstraat 121-B, Amsterdam"
        )
        val place2 = Place(
            "test_db_bar_2",
            52.375233,
            4.880817,
            "Café De Nieuwe Lelie",
            true,
            4.6,
            "Nieuwe Leliestraat 83, Amsterdam"
        )
        val expected = listOf(place1, place2)

        // 2. Action
        placeDao.insertAll(expected)
        val list = placeDao.getAll()

        // 3. Verification
        assertEquals(expected, list)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}
