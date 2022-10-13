package com.example.weather.repository.mainscreen

import com.example.weather.model.CurrentWeatherDTO
import retrofit2.Callback

interface RepositoryHome {
    fun getWeatherFromServer(lat:Double, lon:Double,callBack: Callback<CurrentWeatherDTO>)
}