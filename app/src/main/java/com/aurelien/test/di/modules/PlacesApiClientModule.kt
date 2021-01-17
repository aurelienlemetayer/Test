package com.aurelien.test.di.modules

import com.aurelien.test.services.PlacesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class PlacesApiClientModule {

    @Provides
    @Singleton
    fun providePlacesApi(retrofit: Retrofit) = retrofit.create(PlacesApi::class.java)
}