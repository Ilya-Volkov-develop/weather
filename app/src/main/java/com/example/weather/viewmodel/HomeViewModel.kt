package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.Weather7DTO
import com.example.weather.model.WeatherEveryThreeHoursDTO
import com.example.weather.model.WeatherNowDTO
import com.example.weather.repository.mainscreen.RepositoryRemoteImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val liveData: MutableLiveData<AppStateWeather> = MutableLiveData()): ViewModel() {

    private val repositoryRemoteImpl: RepositoryRemoteImpl by lazy {
        RepositoryRemoteImpl()
    }

    fun getLiveData() = liveData

//    fun saveWeather(position:Int, weather: Weather){
//        repositoriesRoomImpl.saveWeather(position,weather)
//    }

    fun getWeatherNowFromRemoteServer(lat:Double, lon:Double){
        liveData.postValue(AppStateWeather.Loading(0))
        repositoryRemoteImpl.getWeatherNowFromServer(lat,lon,callbackNow)
    }

    fun getWeatherEveryHoursFromRemoteServer(lat:Double, lon:Double){
        liveData.postValue(AppStateWeather.Loading(0))
        repositoryRemoteImpl.getWeatherEveryThreeHoursFromServer(lat,lon,2,callbackEveryThreeHoursDTO)
    }

    fun getWeather7DaysFromRemoteServer(lat:Double, lon:Double){
        liveData.postValue(AppStateWeather.Loading(0))
        repositoryRemoteImpl.getWeather7DaysFromServer(lat,lon,7,callback7)
    }

    private val callbackNow = object : Callback<WeatherNowDTO> {
        override fun onFailure(call: Call<WeatherNowDTO>, t: Throwable) {}

        override fun onResponse(call: Call<WeatherNowDTO>, response: Response<WeatherNowDTO>) {
            if (response.isSuccessful){
                response.body()?.let {
                    liveData.postValue(AppStateWeather.Success(it.response))
                }
            }
        }
    }

    private val callbackEveryThreeHoursDTO = object : Callback<WeatherEveryThreeHoursDTO> {
        override fun onFailure(call: Call<WeatherEveryThreeHoursDTO>, t: Throwable) {}

        override fun onResponse(call: Call<WeatherEveryThreeHoursDTO>, response: Response<WeatherEveryThreeHoursDTO>) {
            if (response.isSuccessful){
                response.body()?.let {
                    liveData.postValue(AppStateWeather.SuccessEveryThreeHours(it.response))
                }
            }
        }
    }

    private val callback7 = object : Callback<Weather7DTO> {
        override fun onFailure(call: Call<Weather7DTO>, t: Throwable) {}

        override fun onResponse(call: Call<Weather7DTO>, response: Response<Weather7DTO>) {
            if (response.isSuccessful){
                response.body()?.let {
                    liveData.postValue(AppStateWeather.Success7(it.response))
                }
            }
        }
    }
}