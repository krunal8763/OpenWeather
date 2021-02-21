package com.krunal.openweather.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.krunal.openweather.R
import com.krunal.openweather.databinding.FragmentSettingsBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SettingsFragment : Fragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val settingsViewModel by activityViewModels<SettingsViewModel> { viewModelFactory }

    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.unitLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> binding.groupUnit.check(R.id.btnMetric)
                else -> binding.groupUnit.check(R.id.btnImperial)
            }
        }
        )
        settingsViewModel.loadDefaults()

        binding.groupUnit.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnImperial -> {
                        settingsViewModel.setImperialAsDefaultUnit()
                    }
                    else -> {
                        settingsViewModel.setMetricAsDefaultUnit()
                    }
                }
            }
        }
    }
}