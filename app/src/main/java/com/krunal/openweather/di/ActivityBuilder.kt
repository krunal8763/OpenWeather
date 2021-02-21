package com.krunal.openweather.di

import com.krunal.openweather.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ActivityBuilder {

    @ContributesAndroidInjector
    abstract fun weatherActivity(): MainActivity
}
