package com.example.weather.view

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.model.Response
import com.example.weather.viewmodel.AppStateWeatherEveryThreeHours
import com.example.weather.viewmodel.AppStateWeatherNow
import com.example.weather.viewmodel.HomeViewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment:Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val lat:Double = 55.755826
    private val lon:Double = 37.617299900000035

    private val viewModel: HomeViewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.weatherImg.loadIconSvg(R.drawable.c3)
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getWeatherNowFromRemoteServer(lat, lon)
        viewModel.getWeatherEveryHoursFromRemoteServer(lat,lon)
//        arguments?.let {
//            it.getParcelable<City>(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT)?.let { city ->
//                localWeather = Weather(city,city.lat.toInt(),city.lon.toInt())
//                viewModel.getWeatherFromRemoteServer(city.lat,city.lon)
//            }
//            position = it.getInt(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT_POSITION)
//        }
    }

    private fun renderData(appStateWeatherNow: Any) {
        when(appStateWeatherNow){
            is AppStateWeatherEveryThreeHours.Success ->{
                Log.d("mylogs",appStateWeatherNow.weatherData.toString())
            }
            is AppStateWeatherNow.Success -> {
                setWeatherData(appStateWeatherNow.weatherData)
                getNameCity(lat,lon)
            }
            else -> {}
        }

    }

    //Устанавливаем данные в фрагмент
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setWeatherData(weather: Response) {
        with(binding){
            temperature.text = weather.temperature.comfort.c.toString()
            speedWind.text = weather.wind.speed.kmH.toString() + " km/h"
            pressure.text = weather.pressure.hPa.toString() + " mmHg"
            humidity.text = weather.humidity.percent.toString() + "%"
            cloudiness.text = "${weather.cloudiness.percent}%"
            conditions.text = weather.description.full
            val sdf = SimpleDateFormat("EEEE | MMM dd")
            val dayOfTheWeek: String = sdf.format(Date())
            date.text = dayOfTheWeek
        }
    }

    //Получаю город по координатам
    private fun getNameCity(lat:Double,lon:Double){
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            var subAdminArea = ""
            var mThoroughfare = ""
            var mSubThoroughfare = ""
            val addresses: List<Address>? = geocoder.getFromLocation(lat, lon, 1)
            if (addresses != null) {
                if (addresses[0].subAdminArea != null){
                    subAdminArea = addresses[0].subAdminArea
                }
                if (addresses[0].thoroughfare != null){
                    mThoroughfare = addresses[0].thoroughfare
                    if (addresses[0].subThoroughfare != null){
                        mSubThoroughfare = addresses[0].subThoroughfare
                    }
                }
                val strReturnedAddress = StringBuilder(
                    "$subAdminArea\n$mThoroughfare $mSubThoroughfare")
                binding.cityName.text = strReturnedAddress.toString()
            } else {
                binding.cityName.text = "Нет адресов!"
            }
        } catch (e: IOException) {
            binding.cityName.text = "Не могу получить адрес!"
        }
    }

    private fun ImageView.loadIconSvg(url: Int){
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry{add(SvgDecoder(this@loadIconSvg.context))}
            .build()

        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }
//
//    //при ошибке всплывает диалоговое окно
//    private fun loadingFailed(textId: Int, code: Int) {
//        val dialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
//        val inflater: LayoutInflater? = LayoutInflater.from(requireContext())
//        val exitView: View = inflater!!.inflate(R.layout.dialog_error, null)
//        dialog.setView(exitView)
//        val dialog1: Dialog? = dialog.create()
//        val ok: Button = exitView.findViewById(R.id.ok)
//        val codeTextView = exitView.findViewById<TextView>(R.id.textError)
//
//        codeTextView.text = when(textId) {
//            R.string.errorOnServer -> getString(R.string.errorOnServer)
//            R.string.codeError -> getString(R.string.codeError) + " " + code
//            else -> ""
//        }
//        dialog1?.setCancelable(false)
//        ok.setOnClickListener {
//            dialog1?.dismiss()
//            requireActivity().onBackPressed()
//        }
//        dialog1?.show()
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        fun newInstance() = HomeFragment()
    }
}