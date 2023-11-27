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
    //VM datamedlemar
    val currentAreaTemperature: StateFlow<String>

    //VM funktioner
    suspend fun fetchWeatherData()
}

class WeatherVM(
    application: Application,
    private val weatherModel: WeatherModel,
    ) :AndroidViewModel(application), WeatherViewModel{

    private val _currentAreaTemperature = MutableStateFlow("-1")
    override val currentAreaTemperature: StateFlow<String>
        get() = _currentAreaTemperature.asStateFlow()

    private val _weatherState = MutableStateFlow<Result<String>>(Result.Loading)
    val weatherState: StateFlow<Result<String>> = _weatherState

    private val _hourlyForecast = MutableStateFlow<List<Hour>>(emptyList())
    val hourlyForecast: StateFlow<List<Hour>> get() = _hourlyForecast

    private val _weeklyForecast = MutableStateFlow<List<Day>>(emptyList())
    val weeklyForecast: StateFlow<List<Day>> get() = _weeklyForecast

    private var _cityToShow = MutableStateFlow<String>("Stockholm")
    val cityToShow: StateFlow<String> get() = _cityToShow



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
        //Overview
//        viewModelScope.launch {
//            _weatherState.value = Result.Loading
//            try {
//                val result= WeatherDataSource.getWeather()
//                if (result is Result.Success){
//                    val f = Forecast(
//                        result.data.approvedTime,
//                        result.data.referenceTime,
//                        Geometry(
//                            result.data.geometry.type,
//                            result.data.geometry.coordinates
//                        ),
//                        result.data.timeSeries
//                    )
////                    val c = f.getCurrentAreaTemperature()
//                    _currentAreaTemperature.update { c.toString() }
//                    //TODO SAVE Weather to db
//                }else{
//                    _weatherState.value = Result.Error(Exception("Failed to fetch data"))
//                }
//            }catch (e: Exception){
//                _weatherState.value = Result.Error(e)
//            }
//        }
    }

    private fun getSavedWeather(){
        //TODO implement this
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
//        Log.d("MyApp",_weeklyForecast.value[1].toString())
//        Log.d("MyApp",_weeklyForecast.value.toString())
    }

    fun setCityToShow(cityToShow:String){
        _cityToShow.value=cityToShow
    }
}

data class Hour(
    val time: String,
    val temperature: String,
    val chanceOfRain: String,
    val iconType: String
)

