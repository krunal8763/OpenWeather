package com.krunal.openweather.domain

import com.krunal.openweather.data.database.WeatherData
import com.krunal.openweather.data.model.DayWiseWeatherResponse
import com.krunal.openweather.data.model.WeatherResponse
import com.krunal.openweather.utils.Response
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface IWeatherRepository {
    fun getCurrentWeather(map: Map<String, String?>): Single<Response<WeatherResponse>>
    fun getCityWeatherDayWise(
        map: Map<String, String?>
    ): Single<Response<DayWiseWeatherResponse>>

    fun addBookmark(weatherData: WeatherData): Completable

    fun getBookmarkCities(): Flowable<List<WeatherData>>

    fun removeBookmark(weatherData: WeatherData?): Completable
}
