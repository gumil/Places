package dev.gumil.places.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.gumil.places.domain.Place

@Database(entities = [Place::class], version = 1)
internal abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}
