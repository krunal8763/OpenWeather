package com.krunal.openweather.cityweather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.krunal.openweather.R
import com.krunal.openweather.databinding.FragmentCityWeatherBinding
import com.krunal.openweather.home.HomeViewModel
import com.krunal.openweather.settings.SettingsViewModel
import com.krunal.openweather.utils.Constants
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject

class CityFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeViewModel by activityViewModels<HomeViewModel> { viewModelFactory }
    private val settingsViewModel by activityViewModels<SettingsViewModel> { viewModelFactory }

    private lateinit var binding: FragmentCityWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCityWeatherBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.loadDefaults()

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.isFocusable = false;
        binding.recyclerView.itemAnimator = DefaultItemAnimator()

        binding.swipeContainer.setOnRefreshListener {
            homeViewModel.getCurrentWeatherForCity(settingsViewModel.getUnit())
        }

        homeViewModel.getCurrentWeatherForCity(settingsViewModel.getUnit())

        homeViewModel.weatherResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                binding.swipeContainer.isRefreshing = false
                binding.contentEmptyLayout.emptyLayout.visibility = View.GONE
                binding.coordinatorLayout.visibility = View.VISIBLE

                val tempTextView: TextView = binding.tempTextView
                val descriptionTextView: TextView = binding.descriptionTextView
                val humidityTextView: TextView = binding.humidityTextView
                val windTextView: TextView = binding.windTextView

                tempTextView.text = String.format(Locale.getDefault(), "%.0f°", it.main.temp)
                descriptionTextView.text = Constants.getWeatherStatus(
                    it.id,
                )
                humidityTextView.text = String.format(
                    Locale.getDefault(),
                    "%d%%",
                    it.main.humidity
                )
                windTextView.text = String.format(
                    Locale.getDefault(),
                    resources.getString(R.string.wind_unit_label),
                    it.wind.speed
                )

                Glide.with(requireActivity())
                    .load("https://openweathermap.org/img/wn/${it.weather[0].icon}@4x.png")
                    .fitCenter()
                    .centerCrop()
                    .into(binding.weatherImage)
            } else {
                binding.contentEmptyLayout.emptyLayout.visibility = View.VISIBLE
                binding.coordinatorLayout.visibility = View.GONE
            }
        })

        homeViewModel.weatherResponseList.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                if (it.isNotEmpty()) {
                    val adapter = FiveDayWeatherAdapter(it)
                    binding.recyclerView.adapter = adapter
                }
            })

        homeViewModel.weatherNotFound.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                binding.swipeContainer.isRefreshing = false
                binding.contentEmptyLayout.emptyLayout.visibility = View.VISIBLE
                binding.coordinatorLayout.visibility = View.GONE
                binding.contentEmptyLayout.searchTextView.text = it.msg
            }
        })
    }

}