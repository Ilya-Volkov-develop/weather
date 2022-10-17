package com.example.weather.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.weather.R
import com.example.weather.databinding.FragmentHomeBinding
import com.example.weather.model.CityDTO
import com.example.weather.model.Response
import com.example.weather.recycleradapter.Weather7DaysAdapter
import com.example.weather.recycleradapter.WeatherEveryThreeHoursAdapter
import com.example.weather.utils.BUNDLE_KEY_SEARCH_IN_HOME
import com.example.weather.utils.REQUEST_CODE
import com.example.weather.viewmodel.AppStateWeather
import com.example.weather.viewmodel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var lat: Double = -1000.0
    private var lon: Double = -1000.0
    private lateinit var cityName:String
    private val weatherList = mutableListOf<Response>()
    private lateinit var sp: SharedPreferences
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var weather:Response

    private val viewModel: HomeViewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }
    private val adapterEveryThreeHours: WeatherEveryThreeHoursAdapter by lazy { WeatherEveryThreeHoursAdapter() }
    private val adapter7Days: Weather7DaysAdapter by lazy { Weather7DaysAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        arguments?.let {
            it.getParcelable<CityDTO>(BUNDLE_KEY_SEARCH_IN_HOME)?.let { cityDTO ->
                lat = cityDTO.coords.lat.toDouble()
                lon = cityDTO.coords.lon.toDouble()
                cityName = cityDTO.name
            }
        }
        init()
    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {
        if (lat != -1000.0 && lon != -1000.0){
            viewModel.getWeatherNowFromRemoteServer(lat, lon)
            binding.cityName.text = cityName
        } else {
            checkPermission()
        }
        binding.weather7RecyclerView.adapter = adapter7Days
        binding.weatherRecyclerView.adapter = adapterEveryThreeHours
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        sp = requireActivity().getSharedPreferences("Setting_preferences", Context.MODE_PRIVATE)
        val sdf = SimpleDateFormat("EEEE | MMM dd")
        val dayOfTheWeek: String = sdf.format(Date())
        binding.date2.text = dayOfTheWeek
        btn_add_weather.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.container,SearchFragment.newInstance(),"search")
                .addToBackStack("search").commit()
        }

        binding.btnSetting.setOnClickListener {
            showPopupMenu(it)
        }
        binding.updateSwipeDown.setOnRefreshListener {
            viewModel.getWeatherNowFromRemoteServer(lat, lon)
            update_swipe_down.isRefreshing = false
        }
        binding.cityName.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        context?.let { it ->
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let { it ->
                            getAddress(it)
                        }
                    }
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialogPermission()
                }
                else -> {
                    myRequestPermission()
                }
            }
        }
    }

    private fun getAddress(location: Location){
        lat = location.latitude
        lon = location.longitude
        viewModel.getWeatherNowFromRemoteServer(lat, lon)
        getNameCity(lat, lon)
    }


    private fun myRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == REQUEST_CODE) {

            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        location?.let { it ->
                            getAddress(it)
                        }
                    }
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showDialogPermission()
                }
                else -> {
                    Log.d("mylogs", "КОНЕЦ")
                }
            }
        }
    }

    private fun showDialogPermission() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к местоположению")
            .setMessage("Доступ к местоположению нужен для отображения погоды в месте, где вы сейчас находитесь")
            .setPositiveButton("Предоставить доступ") { _, _ ->
                myRequestPermission()
            }
            .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showPopupMenu(v: View) {
        val context = ContextThemeWrapper(requireContext(), R.style.MyPopupMenu)
        val popupMenu = PopupMenu(context, v)
        popupMenu.inflate(R.menu.setting_menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> {
                    val bitmapDrawable = binding.weatherImg.drawable as BitmapDrawable
                    val bytes = ByteArrayOutputStream()
                    bitmapDrawable.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path: String =
                        MediaStore.Images.Media.insertImage(requireContext().contentResolver,
                            bitmapDrawable.bitmap,
                            "Title",
                            null)
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "$cityName, ${weather.temperature.air.c}°C, ${weather.description.full}")
                        putExtra(Intent.EXTRA_STREAM,Uri.parse(path))
                        type = "*/*"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                    return@setOnMenuItemClickListener true
                }
                R.id.setting -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .add(R.id.container, SettingFragment.newInstance(), "setting")
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
        when (appStateWeatherNow) {
            is AppStateWeather.Success7 -> {
                adapter7Days.setWeather(appStateWeatherNow.weatherData)
            }

            is AppStateWeather.SuccessEveryThreeHours -> {
                appStateWeatherNow.weatherData.forEach {
                    if (it.date.unix > System.currentTimeMillis() / 1000) {
                        weatherList.add(it)
                    }
                }
                adapterEveryThreeHours.setWeather(weatherList)
                viewModel.getWeather7DaysFromRemoteServer(lat, lon)
            }
            is AppStateWeather.Success -> {
                weatherList.add(appStateWeatherNow.weatherData)
                viewModel.getWeatherEveryHoursFromRemoteServer(lat, lon)
                weather = appStateWeatherNow.weatherData
                setWeatherData(weather)
            }
            else -> {}
        }

    }

    //Устанавливаем данные в фрагмент
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setWeatherData(weather: Response) {
        with(binding) {
            when (sp.getString("temperature", requireActivity().resources.getString(R.string.degree_c))) {
                "°C" -> {
                    temperature.text = weather.temperature.air.c.toString()
                }
                "°F" -> {
                    temperature.text = weather.temperature.air.f.toString()
                }
            }
            when (sp.getString("windSpeed", requireActivity().resources.getString(R.string.km_h))) {
                requireActivity().resources.getString(R.string.km_h) -> {
                    speedWind.text = "${weather.wind.speed.kmH} ${requireActivity().resources.getString(R.string.km_h)}"
                }
                requireActivity().resources.getString(R.string.mi_h) -> {
                    speedWind.text = "${weather.wind.speed.miH} ${requireActivity().resources.getString(R.string.mi_h)}"
                }
                requireActivity().resources.getString(R.string.m_s) -> {
                    speedWind.text = "${weather.wind.speed.mS} ${requireActivity().resources.getString(R.string.m_s)}"
                }
                else -> {
                    speedWind.text = "${weather.wind.speed.kmH} ${requireActivity().resources.getString(R.string.km_h)}"
                }
            }
            when (sp.getString("pressure", requireActivity().resources.getString(R.string.mmhg))) {
                requireActivity().resources.getString(R.string.mmhg) -> {
                    pressure.text =
                        "${weather.pressure.mmHgATM} ${requireActivity().resources.getString(R.string.mmhg)}"
                }
                requireActivity().resources.getString(R.string.inHg) -> {
                    pressure.text =
                        "${weather.pressure.inHg} ${requireActivity().resources.getString(R.string.inHg)}"
                }
                requireActivity().resources.getString(R.string.hPa) -> {
                    pressure.text =
                        "${weather.pressure.hPa} ${requireActivity().resources.getString(R.string.hPa)}"
                }
                else -> {
                    pressure.text =
                        "${weather.pressure.mmHgATM} ${requireActivity().resources.getString(R.string.mmhg)}"
                }
            }

            humidity.text = weather.humidity.percent.toString() + "%"
            cloudiness.text = "${weather.cloudiness.percent}%"
            conditions.text = weather.description.full
            val sdf = SimpleDateFormat("EEEE | MMM dd")
            val dayOfTheWeek: String = sdf.format(Date())
            date.text = dayOfTheWeek

            val resId = requireActivity().resources.getIdentifier(weather.icon,
                "drawable",
                requireActivity().packageName)
            binding.weatherImg.loadIconSvg(resId)
        }
    }

    //Получаю местоположение по координатам
    private fun getNameCity(lat: Double, lon: Double) {
        Thread {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            try {
                var subAdminArea = ""
                var mThoroughfare = ""
                var mSubThoroughfare = ""
                val addresses: List<Address>? = geocoder.getFromLocation(lat, lon, 1)
                if (addresses != null && addresses.isNotEmpty()) {
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
                        cityName = strReturnedAddress.toString()
                        binding.cityName.text = cityName
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

    private fun ImageView.loadIconSvg(url: Int) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadIconSvg.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = HomeFragment()
        fun newInstance(bundle: Bundle) = HomeFragment().apply { arguments = bundle }
    }
}