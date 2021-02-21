package com.krunal.openweather.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.krunal.openweather.MainActivity
import com.krunal.openweather.R
import com.krunal.openweather.data.database.WeatherData
import com.krunal.openweather.databinding.FragmentHomeBinding
import com.krunal.openweather.settings.SettingsViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class HomeFragment : Fragment() {
    private lateinit var bookmarkadapters: BookMarkCityAdapters
    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val homeViewModel by activityViewModels<HomeViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()

        binding.btnAddLocation.setOnClickListener {
            MapFragment.getInstance().show(requireActivity().supportFragmentManager, "map_sheet")
        }

        bookmarkadapters = BookMarkCityAdapters().apply {
            onDelete = { weatherData: WeatherData ->
                homeViewModel.removeBookmark(weatherData)
            }
            onItemClick = {
                homeViewModel.setWeatherCandidate(it)
                (requireActivity() as MainActivity).navController()
                    .navigate(R.id.navigation_city_weather)
            }
        }
        binding.rcvCity.apply {
            adapter = bookmarkadapters
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeLiveData() {
        homeViewModel.getBookmarkList()
        homeViewModel.bookmarkList.observe(viewLifecycleOwner, Observer {
            bookmarkadapters.updateList(it)
        })
    }
}