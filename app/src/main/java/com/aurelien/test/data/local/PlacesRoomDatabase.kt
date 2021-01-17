package com.aurelien.test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aurelien.test.data.models.Place


@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class PlacesRoomDatabase : RoomDatabase() {

    abstract fun placesDao(): PlacesDao

    companion object {
        const val DB_NAME = "navitia_database"
    }
}