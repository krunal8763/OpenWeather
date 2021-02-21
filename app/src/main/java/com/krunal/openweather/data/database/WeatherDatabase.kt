package com.krunal.openweather.data.database


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [WeatherData::class], version = WeatherDatabase.VERSION, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        const val VERSION = 1
    }
}