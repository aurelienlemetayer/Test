package com.aurelien.test.services.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Place(
    val id: String,
    val name: String,
    var isFavorite: Boolean = false
) : Parcelable, Comparable<Place> {
    override fun compareTo(other: Place): Int {
        //I chose to just escape ' for the test but in a real application, I will improve the comparison
        val defaultLocale = Locale.getDefault()
        return name.toLowerCase(defaultLocale)
            .replace("'", " ")
            .compareTo(other.name.toLowerCase(defaultLocale))
    }
}