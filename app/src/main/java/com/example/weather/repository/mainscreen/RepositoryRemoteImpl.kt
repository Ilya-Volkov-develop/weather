package com.example.weather.repository.mainscreen

import com.example.weather.App
import com.example.weather.BuildConfig
import com.example.weather.model.CurrentWeatherDTO
import retrofit2.Callback

class RepositoryRemoteImpl: RepositoryHome {
    override fun getWeatherFromServer(lat:Double, lon:Double, callBack: Callback<CurrentWeatherDTO>) {
        App.retrofit().getWeather(BuildConfig.GISMETEO_API_KEY,lat,lon).enqueue(callBack)
    }
}