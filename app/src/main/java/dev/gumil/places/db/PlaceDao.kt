package dev.gumil.places.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import dev.gumil.places.domain.Place

@Dao
internal interface PlaceDao {
    @Query("SELECT * FROM place")
    suspend fun getAll(): List<Place>

    @Insert(onConflict = REPLACE)
    suspend fun insertAll(places: List<Place>)
}
