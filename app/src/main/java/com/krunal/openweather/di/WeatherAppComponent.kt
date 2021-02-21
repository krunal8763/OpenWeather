package com.krunal.openweather.di

import android.content.Context
import com.krunal.openweather.WeatherApplication

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Component(modules = [AndroidInjectionModule::class,WeatherAppModule::class])
@Singleton
interface WeatherAppComponent {
    fun inject(weatherApp: WeatherApplication)

    @Component.Builder
    abstract class Builder {
        abstract fun build(): WeatherAppComponent
        abstract fun appModule(appModule: WeatherAppModule): Builder

        @BindsInstance
        abstract fun application(context: Context): Builder
    }
}