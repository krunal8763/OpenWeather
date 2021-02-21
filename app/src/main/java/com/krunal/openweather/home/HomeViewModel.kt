package com.krunal.openweather.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krunal.openweather.data.database.WeatherData
import com.krunal.openweather.data.model.WeatherNotFound
import com.krunal.openweather.data.model.WeatherResponse
import com.krunal.openweather.domain.IWeatherRepository
import com.krunal.openweather.utils.Constants
import com.krunal.openweather.utils.Response
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val weatherRepository: IWeatherRepository
) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    val text: LiveData<String> = _text

    val weatherResponse = MutableLiveData<WeatherResponse>().apply {
        value = null
    }

    val weatherResponseList = MutableLiveData<List<WeatherResponse>>().apply {
        value = mutableListOf()
    }

    val weatherNotFound = MutableLiveData<WeatherNotFound>().apply {
        value = null
    }

    val bookmarkList = MutableLiveData<List<WeatherData>>().apply {
        value = mutableListOf()
    }

    var weatherRequestCandidate: WeatherData? = null

    private fun currentWeatherRequestPayload(unit: String): MutableMap<String, String> {
        val city = weatherRequestCandidate?.cityName ?: ""
        return if (city == Constants.UNKNOWN) {
            mutableMapOf(
                "lat" to "${weatherRequestCandidate?.lat}",
                "lon" to "${weatherRequestCandidate?.longitude}",
                "appid" to Constants.APP_KEY,
                "units" to unit
            )
        } else {
            mutableMapOf(
                "q" to city,
                "appid" to Constants.APP_KEY,
                "units" to unit
            )
        }
    }

    fun getCurrentWeatherForCity(unit: String) {
        val map = currentWeatherRequestPayload(unit)

        weatherRequestCandidate?.let {
            compositeDisposable.add(
                weatherRepository
                    .getCurrentWeather(map)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { response ->
                            if (response is Response.Success) {
                                getNextFiveDaysWeather(unit)
                                weatherResponse.postValue(response.data)
                            } else if (response is Response.Error) {
                                weatherNotFound.postValue(
                                    WeatherNotFound(
                                        it.cityName ?: "",
                                        "Weather for ${it.cityName} Not Found"
                                    )
                                )
                            }
                        },
                        {

                        }
                    ))
        }

    }

    private fun nextFiveDaysRequestPayload(
        count: Int = 5,
        unit: String
    ): MutableMap<String, String> {
        val city = weatherRequestCandidate?.cityName ?: ""
        if (city == Constants.UNKNOWN) {
            return mutableMapOf(
                "lat" to "${weatherRequestCandidate?.lat}",
                "lon" to "${weatherRequestCandidate?.longitude}",
                "appid" to Constants.APP_KEY,
                "units" to unit,
                "cnt" to "$count"
            )
        } else {
            return mutableMapOf(
                "q" to city,
                "appid" to Constants.APP_KEY,
                "units" to unit,
                "cnt" to "$count"
            )
        }
    }

    private fun getNextFiveDaysWeather(unit: String) {

        val map = nextFiveDaysRequestPayload(5, unit)

        compositeDisposable.add(weatherRepository
            .getCityWeatherDayWise(map)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it is Response.Success) {
                        weatherResponseList.postValue(it.data.weather.subList(1, 5))
                    } else {

                    }
                },
                {

                }
            ))
    }

    fun addWeatherToBookmark(weatherData: WeatherData): Boolean {
        return if (bookmarkList.value?.contains(weatherData) == false) {
            compositeDisposable.add(
                weatherRepository.addBookmark(weatherData)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        // success
                    }, {
                        // might be an error
                    })
            )
            true
        } else {
            false
        }
    }

    fun removeBookmark(weatherData: WeatherData) {
        compositeDisposable.add(
            weatherRepository.removeBookmark(weatherData).observeOn(AndroidSchedulers.mainThread())
                .subscribe { })
    }

    fun getBookmarkList() {
        compositeDisposable.add(
            weatherRepository
                .getBookmarkCities()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.e("krunal", "getBookmarkList: ${it.size}")
                        if (it.isNotEmpty()) {
                            bookmarkList.postValue(it)
                        }
                    }, {
                        Log.e("krunal", "getBookmarkList: ", it)
                    }
                )
        )
    }

    fun setWeatherCandidate(it: WeatherData) {
        weatherNotFound.value = null
        weatherResponseList.value = mutableListOf()
        weatherResponse.value = null
        weatherRequestCandidate = it
    }

}
