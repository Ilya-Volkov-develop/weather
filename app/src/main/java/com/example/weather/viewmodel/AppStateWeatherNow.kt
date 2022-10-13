package com.example.weather.viewmodel

import com.example.weather.model.Response


sealed class AppStateWeatherNow {
    data class Loading(val progress:Int): AppStateWeatherNow()
    data class Success(var weatherData: Response): AppStateWeatherNow()
    data class Error(val error:Int, val code:Int): AppStateWeatherNow()
}