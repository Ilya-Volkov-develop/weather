package com.example.weather.viewmodel

import com.example.weather.model.Response
import com.example.weather.model.Response7


sealed class AppStateWeather {
    data class Loading(val progress:Int): AppStateWeather()
    data class Error(val error:Int, val code:Int): AppStateWeather()

    data class Success(var weatherData: Response): AppStateWeather()
    data class Success7(var weatherData: List<Response7>): AppStateWeather()
    data class SuccessEveryThreeHours(var weatherData: List<Response>): AppStateWeather()
}