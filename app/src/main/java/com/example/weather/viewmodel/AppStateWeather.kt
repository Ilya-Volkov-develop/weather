package com.example.weather.viewmodel

import com.example.weather.model.Response


sealed class AppStateWeather {
    data class Loading(val progress:Int): AppStateWeather()
    data class Success(var weatherData: Response): AppStateWeather()
    data class Error(val error:Int, val code:Int): AppStateWeather()
}