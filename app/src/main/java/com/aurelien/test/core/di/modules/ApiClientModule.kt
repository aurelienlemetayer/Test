package com.aurelien.test.core.di.modules

import com.aurelien.test.BuildConfig
import com.aurelien.test.core.services.ApiCoroutinesClient
import com.aurelien.test.core.services.ApiTokenInterceptor
import com.aurelien.test.core.utils.CustomDateAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ApiClientModule {

    @Singleton
    @Provides
    fun provideApiCoroutinesClient() = ApiCoroutinesClient()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ApiTokenInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.NAVITIA_API_URL)
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .add(Date::class.java, CustomDateAdapter())
                        .build()
                )
            )
            .build()
    }
}