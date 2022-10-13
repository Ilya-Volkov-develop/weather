package com.example.weather.repository.mainscreen

import com.example.weather.App
import com.example.weather.BuildConfig
import com.example.weather.model.Weather7DTO
import com.example.weather.model.WeatherNowDTO
import com.example.weather.model.WeatherEveryThreeHoursDTO
import retrofit2.Callback

class RepositoryRemoteImpl : RepositoryWeatherServer {
    override fun getWeatherNowFromServer(
        lat: Double,
        lon: Double,
        callBack: Callback<WeatherNowDTO>,
    ) {
        App.retrofit().getWeatherNow(BuildConfig.GISMETEO_API_KEY, lat, lon).enqueue(callBack)
    }

    override fun getWeatherEveryThreeHoursFromServer(
        lat: Double,
        lon: Double,
        day: Int,
        callBack: Callback<WeatherEveryThreeHoursDTO>,
    ) {
        App.retrofit().getWeatherEveryThreeHours(BuildConfig.GISMETEO_API_KEY, lat, lon, day).enqueue(callBack)
    }

    override fun getWeather7DaysFromServer(
        lat: Double,
        lon: Double,
        day: Int,
        callBack: Callback<Weather7DTO>,
    ) {
        App.retrofit().getWeather7Days(BuildConfig.GISMETEO_API_KEY, lat, lon, day).enqueue(callBack)
    }
}