package dev.gumil.places.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class Place(
    @PrimaryKey val id: String,
    @ColumnInfo val latitude: Double,
    @ColumnInfo val longitude: Double,
    @ColumnInfo val name: String,
    @ColumnInfo val isOpen: Boolean,
    @ColumnInfo val rating: Double,
    @ColumnInfo val vicinity: String,
    @ColumnInfo val distance: Float = 0f
)

internal typealias Places = Pair<String?, List<Place>>
