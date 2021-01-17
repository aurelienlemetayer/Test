package com.aurelien.test.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aurelien.test.data.models.Place

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Query("DELETE FROM places WHERE id = :placeId")
    suspend fun delete(placeId: String)

    @Query("SELECT * FROM places ORDER BY name ASC")
    suspend fun getAllPlaces(): List<Place>
}