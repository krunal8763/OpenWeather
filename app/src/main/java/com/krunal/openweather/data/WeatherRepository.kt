package com.krunal.openweather.data

import com.krunal.openweather.data.database.WeatherDao
import com.krunal.openweather.data.database.WeatherData
import com.krunal.openweather.data.model.DayWiseWeatherResponse
import com.krunal.openweather.data.model.WeatherResponse
import com.krunal.openweather.domain.IWeatherRepository
import com.krunal.openweather.utils.Constants
import com.krunal.openweather.utils.Response
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val weatherService: IWeatherService,
    private val weatherDao: WeatherDao
) :
    IWeatherRepository {

    override fun getCurrentWeather(map: Map<String, String?>):
            Single<Response<WeatherResponse>> {

        return weatherService.getCurrentWeather(map)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.cod != Constants.OK) {
                    return@map Response.Error<WeatherResponse>()
                }
                return@map Response.Success(response)
            }
            .onErrorReturn { throwable ->
                return@onErrorReturn Response.Error(
                    throwable.message,
                    throwable
                )
            }
    }

    override fun getCityWeatherDayWise(
        map: Map<String, String?>
    ): Single<Response<DayWiseWeatherResponse>> {



        return weatherService.getCityWeatherDayWise(map)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.code != Constants.OK_STR) {
                    return@map Response.Error<DayWiseWeatherResponse>()
                }
                return@map Response.Success(response)
            }
            .onErrorReturn { throwable ->
                return@onErrorReturn Response.Error(
                    throwable.message,
                    throwable
                )
            }
    }

    override fun addBookmark(weatherData: WeatherData): Completable {
        return weatherDao
            .insertWeatherData(weatherData)
            .subscribeOn(Schedulers.io())
    }

    override fun getBookmarkCities(): Flowable<List<WeatherData>> {
        return weatherDao
            .getAllBookMarks()
            .subscribeOn(Schedulers.io()).map {
                it
            }.onErrorReturn {
                mutableListOf()
            }
    }

    override fun removeBookmark(weatherData: WeatherData?): Completable {
        return weatherDao.deleteBookMark(weatherData).subscribeOn(Schedulers.io())
    }

}