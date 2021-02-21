package com.krunal.openweather.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krunal.openweather.data.IWeatherService
import com.krunal.openweather.data.WeatherRepository
import com.krunal.openweather.domain.IWeatherRepository
import com.krunal.openweather.home.HomeViewModel
import com.krunal.openweather.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import javax.inject.Named

@Module(includes = [WeatherModule::class])
class DIWeatherModule {
    @Provides
    fun provideWeatherService(@Named("weatherApi") retrofit: Retrofit): IWeatherService =
        retrofit.create(IWeatherService::class.java)
}

@Module
interface WeatherModule {
    @Binds
    fun bindsWeatherRepository(weatherRepository: WeatherRepository): IWeatherRepository

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun bindViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun bindSettingViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(
        factory: DaggerViewModelFactory
    ): ViewModelProvider.Factory
}