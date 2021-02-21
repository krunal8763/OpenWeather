package com.krunal.openweather.data.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = WeatherData.TABLE_NAME)
data class WeatherData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int? = null,

    @ColumnInfo(name = CITY_NAME)
    var cityName: String?,

    @ColumnInfo(name = LAT)
    val lat: String?,

    @ColumnInfo(name = LONGITUDE)
    val longitude: String?,
) {
    constructor() : this(0, "", "", "")

    companion object {
        const val TABLE_NAME = "weather_details"
        const val ID = "id"
        const val CITY_NAME = "city_name"
        const val LAT = "latitude"
        const val LONGITUDE = "longitude"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeatherData

        if (cityName != other.cityName) return false

        return true
    }

    override fun hashCode(): Int {
        return cityName?.hashCode() ?: 0
    }
}