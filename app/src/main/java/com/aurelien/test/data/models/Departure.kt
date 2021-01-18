package com.aurelien.test.data.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.aurelien.test.R
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Departure(
    val code: String,
    val color: String,
    val date: Date,
    val commercialMode: CommercialMode,
    val direction: String
) : Parcelable

enum class CommercialMode(
    val value: String,
    @DrawableRes val drawableResId: Int
) {
    RER("RER", R.drawable.ic_rer),
    METRO("Métro", R.drawable.ic_metro),
    BUS("Bus", R.drawable.ic_bus),
    UNKNOWN("unknown", -1)
}