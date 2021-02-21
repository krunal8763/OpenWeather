package com.krunal.openweather.cityweather


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.krunal.openweather.R
import com.krunal.openweather.data.model.WeatherResponse
import com.krunal.openweather.databinding.WeatherDayItemBinding
import com.krunal.openweather.utils.Constants

import java.util.*


class FiveDayWeatherAdapter(private val weatherList: List<WeatherResponse>) :
    RecyclerView.Adapter<FiveDayWeatherAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var colors: IntArray
    private lateinit var binding: WeatherDayItemBinding

    data class ViewHolder(val binding: WeatherDayItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        colors = parent.context.resources.getIntArray(R.array.weather_colors)
        binding = WeatherDayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = weatherList.get(position)
        holder.binding.cardView.setBackgroundColor(colors[position])
        val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar.setTimeInMillis(weather.dt * 1000L)
        binding.dayNameTextView.setText(Constants.DAYS_OF_WEEK[calendar.get(Calendar.DAY_OF_WEEK) - 1])
        if (weather.temp.temp_max < 0 && weather.temp.temp_max > -0.5) {
            weather.temp.temp_max = 0.0
        }
        if (weather.temp.temp_min < 0 && weather.temp.temp_min > -0.5) {
            weather.temp.temp_min = 0.0
        }
        if (weather.temp.temp < 0 && weather.temp.temp > -0.5) {
            weather.temp.temp = 0.0
        }
        binding.tempTextView.setText(String.format(Locale.getDefault(), "%.0f°", weather.temp.temp))
        binding.minTempTextView.setText(
            String.format(
                Locale.getDefault(),
                "%.0f°",
                weather.temp.temp_min
            )
        )
        binding.maxTempTextView.setText(
            String.format(
                Locale.getDefault(),
                "%.0f°",
                weather.temp.temp_max
            )
        )

        val image = "https://openweathermap.org/img/wn/${weather.weather.get(0).icon}@4x.png"
        Log.e("krunal", "onBindViewHolder: $image")
        Glide.with(context)
            .load(image)
            .fitCenter()
            .centerCrop()
            .into(binding.weatherImageView)

    }

    override fun getItemCount(): Int = weatherList.size
}