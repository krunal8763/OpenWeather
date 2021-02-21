package com.krunal.openweather.utils

object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val UNKNOWN = "Unknown"
    const val OK = 200
    const val OK_STR = "200"
    const val APP_KEY = "542ffd081e67f4512b705f89d2a611b2"
    
    val DAYS_OF_WEEK = arrayOf(
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    )

    val WEATHER_STATUS = arrayOf(
        "Thunderstorm",
        "Drizzle",
        "Rain",
        "Snow",
        "Atmosphere",
        "Clear",
        "Few Clouds",
        "Broken Clouds",
        "Cloud"
    )

    /**
     * Get weather status string according to weather status code
     *
     * @param weatherCode weather status code
     * @return String weather status
     */
    fun getWeatherStatus(weatherCode: Int): String {
        if (weatherCode / 100 == 2) {
            return Constants.WEATHER_STATUS[0]
        } else if (weatherCode / 100 == 3) {
            return Constants.WEATHER_STATUS[1]
        } else if (weatherCode / 100 == 5) {
            return Constants.WEATHER_STATUS[2]
        } else if (weatherCode / 100 == 6) {
            return Constants.WEATHER_STATUS[3]
        } else if (weatherCode / 100 == 7) {
            return Constants.WEATHER_STATUS[4]
        } else if (weatherCode == 800) {
            return Constants.WEATHER_STATUS[5]
        } else if (weatherCode == 801) {
            return Constants.WEATHER_STATUS[6]
        } else if (weatherCode == 803) {
            return Constants.WEATHER_STATUS[7]
        } else if (weatherCode / 100 == 8) {
            return Constants.WEATHER_STATUS[8]
        }
        return Constants.WEATHER_STATUS[4]
    }

    const val TIME_LIMIT = 60000 // 1 minute
}