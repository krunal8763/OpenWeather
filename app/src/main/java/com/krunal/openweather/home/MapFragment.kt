package com.krunal.openweather.home

import android.app.AlertDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.krunal.openweather.data.database.WeatherData
import com.krunal.openweather.databinding.FragmentMapBinding
import com.krunal.openweather.utils.Constants
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject


class MapFragment : BottomSheetDialogFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private var weatherData: WeatherData? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val homeViewModel by viewModels<HomeViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddCity.setOnClickListener {
            if (weatherData != null) {
                weatherData?.let {
                    val isValid = homeViewModel.addWeatherToBookmark(it)

                    if (!isValid) {
                        AlertDialog.Builder(requireContext()).apply {
                            setMessage("${it.cityName} City already added to bookmark list , please try to change")
                            show()
                        }
                    } else {
                        dismiss()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please mark location", Toast.LENGTH_SHORT).show()
            }
        }
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)
    }

    private lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapClickListener { point ->

            weatherData =
                WeatherData(
                    cityName = "",
                    lat = point.latitude.toString(),
                    longitude = point.longitude.toString()
                )

            val gcd = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> = gcd.getFromLocation(point.latitude, point.longitude, 1)
            if (addresses.isNotEmpty()) {
                weatherData?.cityName = addresses[0].locality ?: Constants.UNKNOWN
            }

            mMap.clear()
            val addMarker = LatLng(point.latitude, point.longitude)
            mMap.addMarker(
                MarkerOptions()
                    .position(addMarker)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLng(addMarker))

            binding.txtCity.text = weatherData?.cityName
        }


        val cameraPosition =
            CameraPosition.builder().target(LatLng(22.994889704972557, 72.57468950003384)).zoom(10f)
                .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    companion object {
        fun getInstance(): MapFragment {
            return MapFragment()
        }
    }
}