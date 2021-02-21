package com.krunal.openweather.data

import com.krunal.openweather.data.database.WeatherDao
import com.krunal.openweather.data.database.WeatherData
import com.krunal.openweather.data.model.DayWiseWeatherResponse
import com.krunal.openweather.data.model.WeatherResponse
import com.krunal.openweather.utils.ErrorType
import com.krunal.openweather.utils.Response
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest {

    init {
        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        MockKAnnotations.init(this)
        RxAndroidPlugins.reset()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    lateinit var weatherRepository: WeatherRepository

    private val weatherDaoMock = mockk<WeatherDao>()
    private val weatherServiceMock = mockk<IWeatherService>()
    private val weatherResponseMock = mockk<WeatherResponse>()
    private val weatherDataListMock = mockk<List<WeatherData>>()
    private val dayWiseWeatherResponseMock = mockk<DayWiseWeatherResponse>()

    @Before
    fun setUp() {
        weatherRepository = WeatherRepository(weatherServiceMock, weatherDaoMock)
    }

    @Test
    fun `getCurrentWeather success`() {
        every { weatherResponseMock.cod } returns 200
        every { weatherServiceMock.getCurrentWeather(any()) } returns Single.just(
            weatherResponseMock
        )
        every { weatherResponseMock.convertToString() } returns ""
        every { weatherDaoMock.insertWeatherData(any()) } returns Completable.complete()


        weatherRepository.getCurrentWeather(mutableMapOf()).test().assertValue { value ->
            if (value is Response.Success)
                value.data == weatherResponseMock
            else
                false
        }

    }

    @Test
    fun `getCurrentWeather error`() {
        every { weatherServiceMock.getCurrentWeather(any()) } returns Single.error(
            ErrorType(
                ErrorType.ExceptionType.GENERIC,
                "generic error"
            )
        )

        weatherRepository.getCurrentWeather(mutableMapOf()).test().assertValue { value ->
            if (value is Response.Error)
                value.message == "generic error"
            else
                false
        }
    }

    @Test
    fun `getCityWeatherDayWise success`() {
        every { dayWiseWeatherResponseMock.code } returns "200"
        every { weatherServiceMock.getCityWeatherDayWise(any()) } returns Single.just(
            dayWiseWeatherResponseMock
        )

        weatherRepository.getCityWeatherDayWise(mutableMapOf()).test().assertValue { value ->
            if (value is Response.Success)
                value.data == dayWiseWeatherResponseMock
            else
                false
        }
    }

    @Test
    fun `getCityWeatherDayWise error`() {
        every { weatherServiceMock.getCityWeatherDayWise(any()) } returns Single.error(
            ErrorType(
                ErrorType.ExceptionType.GENERIC,
                "generic error"
            )
        )

        weatherRepository.getCityWeatherDayWise(mutableMapOf()).test().assertValue { value ->
            if (value is Response.Error)
                value.message == "generic error"
            else
                false
        }
    }

    @Test
    fun `add to bookmark success`() {
        every { weatherDaoMock.insertWeatherData(any()) } returns Completable.complete()
        weatherRepository.addBookmark(WeatherData(0, "", "", ""))

        verify { weatherDaoMock.insertWeatherData(any()) }
    }

    @Test
    fun `remove from bookmark success`() {
        every { weatherDaoMock.deleteBookMark(any()) } returns Completable.complete()
        weatherRepository.removeBookmark(WeatherData(0, "", "", ""))

        verify { weatherDaoMock.deleteBookMark(any()) }
    }

    @Test
    fun `get bookmark success`() {
        every { weatherDaoMock.getAllBookMarks() } returns Flowable.just(weatherDataListMock)
        weatherRepository.getBookmarkCities().test().assertValue {
            it == weatherDataListMock
        }

        verify { weatherDaoMock.getAllBookMarks() }
    }
}