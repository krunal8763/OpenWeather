package com.krunal.openweather.data

import com.krunal.openweather.data.model.DayWiseWeatherResponse
import com.krunal.openweather.data.model.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IWeatherService {
    @GET("weather")
    fun getCurrentWeather(@QueryMap map: Map<String, String?>): Single<WeatherResponse>

    @GET("forecast/daily")
    fun getCityWeatherDayWise(@QueryMap map: Map<String, String?>): Single<DayWiseWeatherResponse>
}