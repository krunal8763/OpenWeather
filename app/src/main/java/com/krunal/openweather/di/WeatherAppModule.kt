package com.krunal.openweather.di

import androidx.room.Room
import com.krunal.openweather.BuildConfig
import com.krunal.openweather.WeatherApplication
import com.krunal.openweather.data.database.WeatherDao
import com.krunal.openweather.data.database.WeatherDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ActivityBuilder::class, FragmentBuilder::class])
open class WeatherAppModule(private val weatherApplication: WeatherApplication) {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            )
            .build()
    }

    @Provides
    @Named("weatherApi")
    @Singleton
    fun provideMemberApiRetrofitClient(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(
                WeatherApplication.API_URL.toHttpUrlOrNull()
            )
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    private val weatherDatabase: WeatherDatabase

    @Singleton
    @Provides
    fun providesWeatherDatabase(): WeatherDatabase {
        return weatherDatabase
    }

    @Singleton
    @Provides
    fun providesWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.weatherDao()
    }

    init {
        weatherDatabase =
            Room.databaseBuilder(weatherApplication, WeatherDatabase::class.java, "weather-db")
                .build()
    }
}