package com.example.weather.viewmodel

import com.example.weather.model.Response


sealed class AppStateWeatherEveryThreeHours {
    data class Loading(val progress:Int): AppStateWeatherEveryThreeHours()
    data class Success(var weatherData: List<Response>): AppStateWeatherEveryThreeHours()
    data class Error(val error:Int, val code:Int): AppStateWeatherEveryThreeHours()
}