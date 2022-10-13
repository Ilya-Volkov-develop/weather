package com.example.weather.view

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
import com.example.weather.viewmodel.AppStateWeather
import com.example.weather.viewmodel.HomeViewModel


class HomeFragment:Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.weatherImg.loadIconSvg(R.drawable.c3)
        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getWeatherFromRemoteServer(54.35, 52.52)
//        arguments?.let {
//            it.getParcelable<City>(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT)?.let { city ->
//                localWeather = Weather(city,city.lat.toInt(),city.lon.toInt())
//                viewModel.getWeatherFromRemoteServer(city.lat,city.lon)
//            }
//            position = it.getInt(BUNDLE_KEY_MAIN_FRAGMENT_IN_DETAILS_FRAGMENT_POSITION)
//        }
    }

    private fun renderData(appStateWeather: AppStateWeather) {
        with(binding){
            when(appStateWeather){
                is AppStateWeather.Error -> {
//                    loadingFailed(appStateWeather.error,appStateWeather.code)
                }
                is AppStateWeather.Loading -> {
//                    loadingLayout.visibility = View.VISIBLE
                }
                is AppStateWeather.Success -> {
                    Log.d("mylogs",appStateWeather.weatherData.toString())
//                    loadingLayout.visibility = View.GONE
//                    val weather = appStateWeather.weatherData
//                    val condition = convertConditionEngToRus(appStateWeather.condition)
//                    setWeatherData(weather,condition)
                }
            }
        }

    }

    private fun convertConditionEngToRus(condition: String): String {
        return when(condition){
            "clear" -> "ясно"
            "partly-cloudy" -> "малооблачно"
            "cloudy" -> "облачно с прояснениями"
            "overcast" -> "пасмурно"
            "drizzle" -> "морось"
            "light-rain" -> "небольшой дождь"
            "rain" -> "дождь"
            "moderate-rain" -> "умеренно сильный дождь"
            "heavy-rain" -> "сильный дождь"
            "continuous-heavy-rain" -> "длительный сильный дождь"
            "showers" -> "ливень"
            "wet-snow" -> "дождь со снегом"
            "light-snow" -> "небольшой снег"
            "snow" -> "снег"
            "snow-showers" -> "снегопад"
            "hail" -> "град"
            "thunderstorm" -> "гроза"
            "thunderstorm-with-rain" -> "дождь с грозой"
            "thunderstorm-with-hail" -> "гроза с градом"
            else -> ""
        }

    }

//    //Устанавливаем данные в фрагмент
//    @SuppressLint("SetTextI18n")
//    private fun setWeatherData(weather: Weather, conditionText: String) {
//        position?.let {
//            viewModel.saveWeather(it,Weather(City(localWeather.city.nameCity,0.0,0.0),weather.temperature,weather.feelsLike,weather.icon))
//        }
//        with(binding){
//            temperatureLabel.visibility = View.VISIBLE
//            feelsLikeLabel.visibility = View.VISIBLE
//            cityName.text = localWeather.city.nameCity
//            cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"
//            temperatureValue.text = "${weather.temperature}"
//            feelsLikeValue.text = "${weather.feelsLike}"
//            condition.text = conditionText
//            iconWeather.loadIconSvg("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
//        }
//    }
//
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