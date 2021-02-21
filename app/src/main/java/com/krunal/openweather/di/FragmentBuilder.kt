package com.krunal.openweather.di

import com.krunal.openweather.cityweather.CityFragment
import com.krunal.openweather.home.HomeFragment
import com.krunal.openweather.home.MapFragment
import com.krunal.openweather.settings.SettingsFragment
import com.krunal.openweather.settings.SettingsViewModel
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface FragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector(modules = [DIWeatherModule::class])
    abstract fun currentHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [DIWeatherModule::class])
    abstract fun currentMapFragment(): MapFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [DIWeatherModule::class])
    abstract fun currentCityFragment(): CityFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [DIWeatherModule::class])
    abstract fun currentSettingsFragment(): SettingsFragment
}