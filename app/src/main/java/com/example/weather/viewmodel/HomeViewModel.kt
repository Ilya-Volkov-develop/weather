package com.example.weather.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.CurrentWeatherDTO
import com.example.weather.repository.mainscreen.RepositoryRemoteImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val liveData: MutableLiveData<AppStateWeather> = MutableLiveData()): ViewModel() {

    private val repositoryRemoteImpl: RepositoryRemoteImpl by lazy {
        RepositoryRemoteImpl()
    }
//
//    private val repositoriesRoomImpl: RepositoriesRoomImpl by lazy {
//        RepositoriesRoomImpl()
//    }

    fun getLiveData() = liveData

//    fun saveWeather(position:Int, weather: Weather){
//        repositoriesRoomImpl.saveWeather(position,weather)
//    }

    fun getWeatherFromRemoteServer(lat:Double,lon:Double){
        liveData.postValue(AppStateWeather.Loading(0))
        repositoryRemoteImpl.getWeatherFromServer(lat,lon,callback)
    }
//
//    fun convertDTOtoModel(weatherDTO: WeatherDTO):Weather{
//        return Weather(getDefaultCity(),weatherDTO.fact.temp.toInt(),weatherDTO.fact.feelsLike.toInt(),weatherDTO.fact.icon)
//    }
//
    private val callback = object : Callback<CurrentWeatherDTO> {
        override fun onFailure(call: Call<CurrentWeatherDTO>, t: Throwable) {
            Log.d("mylogs","$call $t")
//            liveData.postValue(AppStateWeather.Error(R.string.errorOnServer,0))
        }

        override fun onResponse(call: Call<CurrentWeatherDTO>, response: Response<CurrentWeatherDTO>) {
            if (response.isSuccessful){
                response.body()?.let {
                    liveData.postValue(AppStateWeather.Success(it.response))
                }
            } else{
//                liveData.postValue(AppStateWeather.Error(R.string.codeError,response.code()))
            }
        }
    }
}