package com.example.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.WeatherEveryThreeHoursDTO
import com.example.weather.model.WeatherNowDTO
import com.example.weather.repository.mainscreen.RepositoryRemoteImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val liveData: MutableLiveData<Any> = MutableLiveData()): ViewModel() {

    private val repositoryRemoteImpl: RepositoryRemoteImpl by lazy {
        RepositoryRemoteImpl()
    }

    fun getLiveData() = liveData

//    fun saveWeather(position:Int, weather: Weather){
//        repositoriesRoomImpl.saveWeather(position,weather)
//    }

    fun getWeatherNowFromRemoteServer(lat:Double, lon:Double){
        liveData.postValue(AppStateWeatherNow.Loading(0))
        repositoryRemoteImpl.getWeatherNowFromServer(lat,lon,callbackNow)
    }

    fun getWeatherEveryHoursFromRemoteServer(lat:Double, lon:Double){
        liveData.postValue(AppStateWeatherNow.Loading(0))
        repositoryRemoteImpl.getWeatherEveryThreeHoursFromServer(lat,lon,1,callbackEveryThreeHoursDTO)
    }

    private val callbackNow = object : Callback<WeatherNowDTO> {
        override fun onFailure(call: Call<WeatherNowDTO>, t: Throwable) {}

        override fun onResponse(call: Call<WeatherNowDTO>, response: Response<WeatherNowDTO>) {
            if (response.isSuccessful){
                response.body()?.let {
                    liveData.postValue(AppStateWeatherNow.Success(it.response))
                }
            }
        }
    }

    private val callbackEveryThreeHoursDTO = object : Callback<WeatherEveryThreeHoursDTO> {
        override fun onFailure(call: Call<WeatherEveryThreeHoursDTO>, t: Throwable) {}

        override fun onResponse(call: Call<WeatherEveryThreeHoursDTO>, response: Response<WeatherEveryThreeHoursDTO>) {
            if (response.isSuccessful){
                response.body()?.let {
                    liveData.postValue(AppStateWeatherEveryThreeHours.Success(it.response))
                }
            }
        }
    }
}