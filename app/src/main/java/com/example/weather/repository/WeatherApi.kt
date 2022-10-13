package com.example.weather.repository

import com.example.weather.model.CurrentWeatherDTO
import com.example.weather.utils.API_KEY_NAME
import com.example.weather.utils.WEATHER_API_URL_END_POINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {
    @GET(WEATHER_API_URL_END_POINT)
    fun getWeather(
        @Header(API_KEY_NAME) apikey:String,
        @Query("latitude") lat:Double,
        @Query("longitude") lon:Double
    ): Call<CurrentWeatherDTO>
}