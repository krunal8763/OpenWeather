package com.krunal.openweather.settings

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {

    val unitLiveData = MutableLiveData<Int>().apply {
        value = UNITS.first
    }

    fun setImperialAsDefaultUnit() {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putInt(UNIT_KEY, UNITS.second)
            commit()
        }
    }

    fun setMetricAsDefaultUnit() {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putInt(UNIT_KEY, UNITS.first)
            commit()
        }
    }

    fun loadDefaults() {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        unitLiveData.value = sharedPreferences.getInt(UNIT_KEY, UNITS.first)
    }

    fun getUnit(): String {
        return when (unitLiveData.value) {
            0 -> "metric"
            else -> "imperial"
        }
    }


    companion object {
        private const val SHARED_PREF_NAME = "WeatherCont"
        private const val UNIT_KEY = "unit"
        private val UNITS = Pair(0, 1) // 0 for metric , 1 for imperial
    }
}