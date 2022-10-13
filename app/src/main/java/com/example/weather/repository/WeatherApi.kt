package com.example.weather.repository

import com.example.weather.model.WeatherNowDTO
import com.example.weather.model.WeatherEveryThreeHoursDTO
import com.example.weather.utils.API_KEY_NAME
import com.example.weather.utils.WEATHER_EVERY_THREE_HOURS_API_URL_END_POINT
import com.example.weather.utils.WEATHER_NOW_API_URL_END_POINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {
    @GET(WEATHER_NOW_API_URL_END_POINT)
    fun getWeatherNow(
        @Header(API_KEY_NAME) apikey:String,
        @Query("latitude") lat:Double,
        @Query("longitude") lon:Double
    ): Call<WeatherNowDTO>

    @GET(WEATHER_EVERY_THREE_HOURS_API_URL_END_POINT)
    fun getWeatherEveryThreeHours(
        @Header(API_KEY_NAME) apikey:String,
        @Query("latitude") lat:Double,
        @Query("longitude") lon:Double,
        @Query("days") days:Int
    ): Call<WeatherEveryThreeHoursDTO>
}