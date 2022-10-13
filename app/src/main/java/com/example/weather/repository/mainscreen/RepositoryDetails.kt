package com.example.weather.repository.mainscreen

import com.example.weather.model.WeatherNowDTO
import com.example.weather.model.WeatherEveryThreeHoursDTO
import retrofit2.Callback

interface RepositoryWeatherServer {
    fun getWeatherNowFromServer(lat:Double, lon:Double,callBack: Callback<WeatherNowDTO>)
    fun getWeatherEveryThreeHoursFromServer(lat:Double, lon:Double, day:Int, callBack: Callback<WeatherEveryThreeHoursDTO>)
}