package com.example.weather.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.model.Response
import com.example.weather.recycleradapter.Weather7DaysAdapter
import com.example.weather.recycleradapter.WeatherEveryThreeHoursAdapter
import com.example.weather.viewmodel.AppStateWeather
import com.example.weather.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment:Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val lat:Double = 55.755811
    private val lon:Double = 37.617617
    private val weatherList = mutableListOf<Response>()
    private lateinit var sp: SharedPreferences

    private val viewModel: HomeViewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }
    private val adapterEveryThreeHours: WeatherEveryThreeHoursAdapter by lazy { WeatherEveryThreeHoursAdapter() }
    private val adapter7Days: Weather7DaysAdapter by lazy { Weather7DaysAdapter() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {
        sp = requireActivity().getSharedPreferences("Setting_preferences", Context.MODE_PRIVATE)
        val sdf = SimpleDateFormat("EEEE | MMM dd")
        val dayOfTheWeek: String = sdf.format(Date())
        binding.date2.text = dayOfTheWeek

        binding.weather7RecyclerView.adapter = adapter7Days
        binding.weatherRecyclerView.adapter = adapterEveryThreeHours
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getWeatherNowFromRemoteServer(lat, lon)

        btn_setting.setOnClickListener {
            showPopupMenu(it)
        }

        //        arguments?.let {
//            it.getParcelable<City>(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT)?.let { city ->
//                localWeather = Weather(city,city.lat.toInt(),city.lon.toInt())
//                viewModel.getWeatherFromRemoteServer(city.lat,city.lon)
//            }
//            position = it.getInt(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT_POSITION)
//        }
    }

    private fun showPopupMenu(v: View) {
        val context = ContextThemeWrapper(requireContext(),R.style.MyPopupMenu)
        val popupMenu = PopupMenu(context, v)
        popupMenu.inflate(R.menu.setting_menu)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.share -> {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Text")
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
                    startActivity(Intent.createChooser(sharingIntent, requireActivity().resources.getString(R.string.share)))
                    return@setOnMenuItemClickListener true
                }
                R.id.setting -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(R.id.container, SettingFragment.newInstance(),"setting")
                        .addToBackStack("setting").commit()
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }
        popupMenu.show()
    }

    private fun renderData(appStateWeatherNow: Any) {
        when(appStateWeatherNow){
            is AppStateWeather.Success7 -> {
                adapter7Days.setWeather(appStateWeatherNow.weatherData)
            }

            is AppStateWeather.SuccessEveryThreeHours ->{
                appStateWeatherNow.weatherData.forEach {
                    if (it.date.unix > System.currentTimeMillis()/1000) {
                        weatherList.add(it)
                    }
                }
                adapterEveryThreeHours.setWeather(weatherList)
                viewModel.getWeather7DaysFromRemoteServer(lat, lon)
            }
            is AppStateWeather.Success -> {
                weatherList.add(appStateWeatherNow.weatherData)
                viewModel.getWeatherEveryHoursFromRemoteServer(lat,lon)
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
            when(sp.getString("temperature","")){
                "°C"->{
                    temperature.text = weather.temperature.comfort.c.toString()
                }
                "°F"->{
                    temperature.text = weather.temperature.comfort.f.toString()
                }
            }
            when(sp.getString("windSpeed",requireActivity().resources.getString(R.string.km_h))){
                requireActivity().resources.getString(R.string.km_h) -> {
                    speedWind.text = weather.wind.speed.kmH.toString() + " km/h"
                }
                requireActivity().resources.getString(R.string.mi_h) -> {
                    speedWind.text = weather.wind.speed.miH.toString() + " mi/h"
                }
                requireActivity().resources.getString(R.string.m_s) -> {
                    speedWind.text = weather.wind.speed.mS.toString() + " m/s"
                }
                else ->{
                    speedWind.text = weather.wind.speed.kmH.toString() + " km/h"
                }
            }
            when(sp.getString("pressure",requireActivity().resources.getString(R.string.mmhg))){
                requireActivity().resources.getString(R.string.mmhg) ->{
                    pressure.text = "${weather.pressure.mmHgATM} ${requireActivity().resources.getString(R.string.mmhg)}"
                }
                requireActivity().resources.getString(R.string.inHg) ->{
                    pressure.text = "${weather.pressure.inHg} ${requireActivity().resources.getString(R.string.inHg)}"
                }
                requireActivity().resources.getString(R.string.hPa) ->{
                    pressure.text = "${weather.pressure.hPa} ${requireActivity().resources.getString(R.string.hPa)}"
                }
                else -> {
                    pressure.text = "${weather.pressure.mmHgATM} ${requireActivity().resources.getString(R.string.mmhg)}"
                }
            }

            humidity.text = weather.humidity.percent.toString() + "%"
            cloudiness.text = "${weather.cloudiness.percent}%"
            conditions.text = weather.description.full
            val sdf = SimpleDateFormat("EEEE | MMM dd")
            val dayOfTheWeek: String = sdf.format(Date())
            date.text = dayOfTheWeek

            val resId = requireActivity().resources.getIdentifier(weather.icon, "drawable", requireActivity().packageName)
            binding.weatherImg.loadIconSvg(resId)
        }
    }

    //Получаю город по координатам
    private fun getNameCity(lat:Double,lon:Double){
        Thread {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            try {
                var subAdminArea = ""
                var mThoroughfare = ""
                var mSubThoroughfare = ""
                val addresses: List<Address>? = geocoder.getFromLocation(lat, lon, 1)
                if (addresses != null) {
                    if (addresses[0].subAdminArea != null) {
                        subAdminArea = addresses[0].subAdminArea
                    }
                    if (addresses[0].thoroughfare != null) {
                        mThoroughfare = addresses[0].thoroughfare
                        if (addresses[0].subThoroughfare != null) {
                            mSubThoroughfare = addresses[0].subThoroughfare
                        }
                    }
                    val strReturnedAddress = StringBuilder(
                        "$subAdminArea\n$mThoroughfare $mSubThoroughfare")
                    requireActivity().runOnUiThread {
                        binding.cityName.text = strReturnedAddress.toString()
                    }
                } else {
                    requireActivity().runOnUiThread {
                        binding.cityName.text = "Нет адресов!"
                    }
                }
            } catch (e: IOException) {
                requireActivity().runOnUiThread {
                    binding.cityName.text = "Не могу получить адрес!"
                }
            }
        }.start()
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