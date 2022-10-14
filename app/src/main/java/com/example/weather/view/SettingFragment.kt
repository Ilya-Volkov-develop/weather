package com.example.weather.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
    private lateinit var sp:SharedPreferences

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
        sp = requireActivity().getSharedPreferences("Setting_preferences", Context.MODE_PRIVATE)
        with(binding){
            temperatureUnitResult.text = sp.getString("temperature","°C")
            windSpeedUnitResult.text = sp.getString("windSpeed",requireActivity().resources.getString(R.string.km_h))
            pressureUnitResult.text = sp.getString("pressure",requireActivity().resources.getString(R.string.mbar))
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
                    sp.edit().putString("temperature","°C").apply()
                    return@setOnMenuItemClickListener true
                }
                R.id.f -> {
                    binding.temperatureUnitResult.text = "°F"
                    sp.edit().putString("temperature","°F").apply()
                    return@setOnMenuItemClickListener true
                }
                R.id.km -> {
                    binding.windSpeedUnitResult.text = requireActivity().resources.getString(R.string.km_h)
                    sp.edit().putString("windSpeed",requireActivity().resources.getString(R.string.km_h)).apply()
                    return@setOnMenuItemClickListener true
                }
                R.id.mi -> {
                    binding.windSpeedUnitResult.text = requireActivity().resources.getString(R.string.mi_h)
                    sp.edit().putString("windSpeed",requireActivity().resources.getString(R.string.mi_h)).apply()
                    return@setOnMenuItemClickListener true
                }
                R.id.m -> {
                    binding.windSpeedUnitResult.text = requireActivity().resources.getString(R.string.m_s)
                    sp.edit().putString("windSpeed",requireActivity().resources.getString(R.string.m_s)).apply()
                    return@setOnMenuItemClickListener true
                }
//                R.id.kn -> {
//                    binding.windSpeedUnitResult.text = requireActivity().resources.getString(R.string.kn)
//                    sp.edit().putString("windSpeed",requireActivity().resources.getString(R.string.kn)).apply()
//                    return@setOnMenuItemClickListener true
//                }
//                R.id.mbar -> {
//                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.mbar)
//                    sp.edit().putString("pressure",requireActivity().resources.getString(R.string.mbar)).apply()
//                    return@setOnMenuItemClickListener true
//                }
//                R.id.atm -> {
//                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.atm)
//                    sp.edit().putString("pressure",requireActivity().resources.getString(R.string.atm)).apply()
//                    return@setOnMenuItemClickListener true
//                }
                R.id.mmHg -> {
                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.mmhg)
                    sp.edit().putString("pressure",requireActivity().resources.getString(R.string.mmhg)).apply()
                    return@setOnMenuItemClickListener true
                }
                R.id.inHg -> {
                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.inHg)
                    sp.edit().putString("pressure",requireActivity().resources.getString(R.string.inHg)).apply()
                    return@setOnMenuItemClickListener true
                }
                R.id.hPa -> {
                    binding.pressureUnitResult.text = requireActivity().resources.getString(R.string.hPa)
                    sp.edit().putString("pressure",requireActivity().resources.getString(R.string.hPa)).apply()
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