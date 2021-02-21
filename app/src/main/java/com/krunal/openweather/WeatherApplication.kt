package com.krunal.openweather

import android.app.Application
import com.krunal.openweather.di.DaggerWeatherAppComponent
import com.krunal.openweather.di.WeatherAppComponent
import com.krunal.openweather.di.WeatherAppModule
import com.krunal.openweather.utils.Constants
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class WeatherApplication : Application(), HasAndroidInjector {
    companion object {
        lateinit var component: WeatherAppComponent
        lateinit var API_URL: String
    }

    override fun onCreate() {
        super.onCreate()

        API_URL = Constants.BASE_URL
        component = DaggerWeatherAppComponent
            .builder()
            .appModule(WeatherAppModule(this))
            .application(applicationContext)
            .build()

        component.inject(this)
    }

    @Inject
    open lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = activityDispatchingAndroidInjector
}