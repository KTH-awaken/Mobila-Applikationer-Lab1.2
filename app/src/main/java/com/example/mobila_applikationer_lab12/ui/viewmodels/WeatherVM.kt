package com.example.mobila_applikationer_lab12.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mobila_applikationer_lab12.model.data.Day
import com.example.mobila_applikationer_lab12.model.data.WeatherModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.mobila_applikationer_lab12.utils.Result
import kotlinx.coroutines.runBlocking

interface WeatherViewModel{
    //VM funktioner
    suspend fun fetchWeatherData()
}

class WeatherVM(
    application: Application,
    private val weatherModel: WeatherModel,
    ) :AndroidViewModel(application), WeatherViewModel{


    private val _hourlyForecast = MutableStateFlow<List<Hour>>(emptyList())
    val hourlyForecast: StateFlow<List<Hour>> get() = _hourlyForecast

    private val _weeklyForecast = MutableStateFlow<List<Day>>(emptyList())
    val weeklyForecast: StateFlow<List<Day>> get() = _weeklyForecast

    private var _cityToShow = MutableStateFlow<String>("Stockholm")
    val cityToShow: StateFlow<String> get() = _cityToShow

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> get() = _favorites



    init {
        runBlocking{
            fetchWeatherData()
        }
        getSavedWeather()
    }

    override suspend fun fetchWeatherData() {
        //Hourly
        updateHourlyForecast()
        //Weekly
        updateWeeklyForecast()


    }


    private fun updateHourlyForecast() {
        val rawForecast = weatherModel.getHourlyForecast(cityToShow.value)
        val hourList = rawForecast.map { timeSeries ->
            Hour(
                time = timeSeries.validTime,
                temperature = "${timeSeries.parameters.find { it.name == "t" }?.values?.get(0)}",
                chanceOfRain = "${timeSeries.parameters.find { it.name == "pcat" }?.values?.get(0)}",
                iconType = weatherModel.determineIconType(timeSeries.parameters)
            )
        }
        _hourlyForecast.value = hourList
    }

    private suspend fun updateWeeklyForecast(){
        val forecast = weatherModel.getWeeklyForecast(cityToShow.value)
        _weeklyForecast.value = forecast
    }

    fun setCityToShow(cityToShow:String){
        _cityToShow.value=cityToShow
    }

    fun addToFavorites(cityName: String){

        //todo implement
    }
    fun removeFromFavorites(cityName: String){
        //todo implement
    }
    private fun getSavedWeather(){
        //TODO implement this
        //_weeklyForecast todo init this
        //_hourlyForecast
    }
    fun  isCityFavorite(cityName: String):Boolean{
        return favorites.value.any { it.cityName == cityName }
    }

    private fun saveAllData(){
//        _weeklyForecast todo save this
//        _hourlyForecast

    }


}

data class Hour(
    val time: String,
    val temperature: String,
    val chanceOfRain: String,
    val iconType: String
)

data class Favorite(
    val cityName:String,
    val day: Day,
)

