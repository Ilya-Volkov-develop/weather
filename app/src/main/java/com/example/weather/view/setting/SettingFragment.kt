package com.example.weather.view.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentSettingBinding


class SettingFragment:Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {
        with(binding){
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            containerTemperature.setOnClickListener {
                showPopupMenu(it,R.menu.temperature_menu)
            }
            containerWindSpeed.setOnClickListener {
                showPopupMenu(it,R.menu.wind_speed_menu)
            }
            containerAtmosphericPressure.setOnClickListener {
                showPopupMenu(it,R.menu.pressure_menu)
            }
        }
    }

    private fun showPopupMenu(view: View, menu: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.c -> {
                    binding.temperatureUnitResult.text = "°C"
                    return@setOnMenuItemClickListener true
                }
                R.id.f -> {
                    binding.temperatureUnitResult.text = "°F"
                    return@setOnMenuItemClickListener true
                }
                R.id.km -> {
                    binding.windSpeedUnitResult.text = requireActivity().resources.getString(R.string.km_h)
                    return@setOnMenuItemClickListener true
                }
                R.id.mi -> {
                    binding.windSpeedUnitResult.text = requireActivity().resources.getString(R.string.mi_h)
                    return@setOnMenuItemClickListener true
                }
                R.id.m -> {
                    binding.windSpeedUnitResult.text = requireActivity().resources.getString(R.string.m_s)
                    return@setOnMenuItemClickListener true
                }
                R.id.kn -> {
                    binding.windSpeedUnitResult.text = requireActivity().resources.getString(R.string.kn)
                    return@setOnMenuItemClickListener true
                }
                R.id.mbar -> {
                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.mbar)
                    return@setOnMenuItemClickListener true
                }
                R.id.atm -> {
                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.atm)
                    return@setOnMenuItemClickListener true
                }
                R.id.mmHg -> {
                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.mmhg)
                    return@setOnMenuItemClickListener true
                }
                R.id.inHg -> {
                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.inHg)
                    return@setOnMenuItemClickListener true
                }
                R.id.hPa -> {
                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.hPa)
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }
        popupMenu.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        fun newInstance() = SettingFragment()
    }
}