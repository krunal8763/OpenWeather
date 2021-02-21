package com.krunal.openweather.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi

@JsonClass(generateAdapter = true)
data class DayWiseWeatherResponse(
    @Json(name = "list") val weather: List<WeatherResponse>,
    @Json(name = "cod") val code: String,
    @Json(name = "cnt") val count: Int
)

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "coord") val coord: Coord = Coord(),
    @Json(name = "weather") val weather: List<Weather> = mutableListOf(),
    @Json(name = "base") val base: String = "",
    @Json(name = "main") val main: Main = Main(),
    @Json(name = "visibility") val visibility: Int = 0,
    @Json(name = "wind") val wind: Wind = Wind(),
    @Json(name = "dt") val dt: Int = 0,
    @Json(name = "sys") val sys: Sys = Sys(),
    @Json(name = "timezone") val timezone: Int = 0,
    @Json(name = "id") val id: Int = 0,
    @Json(name = "name") val name: String = "",
    @Json(name = "cod") val cod: Int = 0,
    @Json(name = "temp") val temp: Temp = Temp()
) {
    fun convertToString(): String {
        return Moshi.Builder().build().adapter(WeatherResponse::class.java).toJson(this)
    }
}

@JsonClass(generateAdapter = true)
data class Temp(
    @Json(name = "day") var temp: Double = 0.0,
    @Json(name = "min") var temp_min: Double = 0.0,
    @Json(name = "max") var temp_max: Double = 0.0,
)

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "temp") var temp: Double = 0.0,
    @Json(name = "feels_like") val feels_like: Double = 0.0,
    @Json(name = "temp_min") var temp_min: Double = 0.0,
    @Json(name = "temp_max") var temp_max: Double = 0.0,
    @Json(name = "pressure") val pressure: Int = 0,
    @Json(name = "humidity") val humidity: Int = 0
)


@JsonClass(generateAdapter = true)
data class Sys(
    @Json(name = "type") val type: Int = 0,
    @Json(name = "id") val id: Int = 0,
    @Json(name = "country") val country: String = "",
    @Json(name = "sunrise") val sunrise: Int = 0,
    @Json(name = "sunset") val sunset: Int = 0
)


@JsonClass(generateAdapter = true)
data class Weather(
    @Json(name = "id") val id: Int = 0,
    @Json(name = "main") val main: String = "",
    @Json(name = "description") val description: String = "",
    @Json(name = "icon") val icon: String = ""
)


@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed") val speed: Double = 0.0,
    @Json(name = "deg") val deg: Int = 0
)


@JsonClass(generateAdapter = true)
data class Clouds(
    @Json(name = "all") val all: Int = 0
)

@JsonClass(generateAdapter = true)
data class Coord(
    @Json(name = "lon") val lon: Double = 0.0,
    @Json(name = "lat") val lat: Double = 0.0
)