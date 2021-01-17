package com.aurelien.test.di.modules

import android.content.Context
import androidx.room.Room
import com.aurelien.test.data.remote.PlacesApi
import com.aurelien.test.data.local.PlacesDao
import com.aurelien.test.data.local.PlacesRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class PlacesClientModule {

    @Provides
    @Singleton
    fun providePlacesApi(retrofit: Retrofit) = retrofit.create(PlacesApi::class.java)

    @Provides
    @Singleton
    fun providePlacesDao(placesRoomDatabase: PlacesRoomDatabase): PlacesDao {
        return placesRoomDatabase.placesDao()
    }

    @Provides
    @Singleton
    fun providePlacesRoomDatabase(@ApplicationContext appContext: Context): PlacesRoomDatabase {
        return Room.databaseBuilder(
            appContext,
            PlacesRoomDatabase::class.java,
            PlacesRoomDatabase.DB_NAME
        ).build()
    }
}