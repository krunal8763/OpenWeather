package com.krunal.openweather.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.krunal.openweather.data.database.WeatherData
import com.krunal.openweather.data.model.DayWiseWeatherResponse
import com.krunal.openweather.data.model.WeatherNotFound
import com.krunal.openweather.data.model.WeatherResponse
import com.krunal.openweather.domain.IWeatherRepository
import com.krunal.openweather.utils.Response
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest : TestCase() {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    init {
        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        MockKAnnotations.init(this)
        RxAndroidPlugins.reset()
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    lateinit var homeViewModel: HomeViewModel
    val weatherRepository = mockk<IWeatherRepository>()

    val weatherResponseMock = mockk<WeatherResponse>()
    val weatherResponseListMock = mockk<List<WeatherResponse>>()
    val dayWiseWeatherResponseMock = mockk<DayWiseWeatherResponse>()
    val weatherNotFoundMock = mockk<WeatherNotFound>()

    @Before
    fun setup() {
        homeViewModel = HomeViewModel(weatherRepository)
    }

    @Test
    fun `getCurrentWeatherForCity success`() {
        success()
        homeViewModel.getCurrentWeatherForCity("metric")
        assert(homeViewModel.weatherResponse.value is WeatherResponse)
        assert(homeViewModel.weatherResponse.value == weatherResponseMock)
        assert(homeViewModel.weatherResponseList.value == weatherResponseListMock)
        assert(homeViewModel.weatherNotFound.value == null)
    }


    @Test
    fun `getCurrentWeatherForCity error`() {
        failure()
        homeViewModel.getCurrentWeatherForCity("metric")
        assert(homeViewModel.weatherNotFound.value is WeatherNotFound)
        assert(
            homeViewModel.weatherNotFound.value?.msg?.startsWith("Weather for")
                ?: false
        )
    }

    @Test
    fun `add to bookmark success`() {
        every { weatherRepository.addBookmark(any()) } returns Completable.complete()

        weatherRepository.addBookmark(mockk()).test().assertComplete()
    }

    @Test
    fun `remove from bookmark success`() {
        every { weatherRepository.removeBookmark(any()) } returns Completable.complete()

        weatherRepository.removeBookmark(mockk()).test().assertComplete()
    }

    private fun success() {
        homeViewModel.weatherRequestCandidate = WeatherData(0, "", "", "")

        every { weatherRepository.getCurrentWeather(any()) } returns Single.just(
            Response.Success(
                weatherResponseMock
            )
        )
        every {
            weatherRepository.getCityWeatherDayWise(
                any()
            )
        } returns Single.just(Response.Success(dayWiseWeatherResponseMock))

        every { dayWiseWeatherResponseMock.weather } returns weatherResponseListMock
        every { weatherResponseListMock.subList(any(), any()) } returns weatherResponseListMock

    }

    private fun failure() {
        homeViewModel.weatherRequestCandidate = WeatherData(0, "", "", "")

        every { weatherRepository.getCurrentWeather(any()) } returns Single.just(
            Response.Error(
                ""
            )
        )
    }
}