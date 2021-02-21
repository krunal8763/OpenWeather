package com.krunal.openweather.data.database


import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherData(data: WeatherData?): Completable

    @Query("SELECT * FROM ${WeatherData.TABLE_NAME}")
    fun getAllBookMarks(): Flowable<List<WeatherData>>

    @Delete
    fun deleteBookMark(weatherData: WeatherData?): Completable
}