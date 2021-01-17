package com.aurelien.test.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity(tableName = "places")
@Parcelize
data class Place(
    @PrimaryKey
    val id: String,
    val name: String,
    var isFavorite: Boolean = false
) : Parcelable, Comparable<Place> {
    override fun compareTo(other: Place): Int {
        val defaultLocale = Locale.getDefault()
        return name.toLowerCase(defaultLocale)
            .compareTo(other.name.toLowerCase(defaultLocale))
    }
}