package com.example.weather

import android.app.Application
import com.example.weather.repository.WeatherApi
import com.example.weather.utils.WEATHER_API_URL
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class App : Application() {
    companion object {
        fun retrofit(): WeatherApi {
            return Retrofit.Builder()
                .baseUrl(WEATHER_API_URL)
                .addConverterFactory(GsonConverterFactory.create(
                    GsonBuilder().create()
                ))
                .build().create(WeatherApi::class.java)
        }
    }
}