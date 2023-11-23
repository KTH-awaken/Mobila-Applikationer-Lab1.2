package com.example.mobila_applikationer_lab12.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobila_applikationer_lab12.model.data.Forecast
import com.example.mobila_applikationer_lab12.model.data.Geometry
import com.example.mobila_applikationer_lab12.networking.JokeDataSource
import com.example.mobila_applikationer_lab12.networking.WeatherDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.mobila_applikationer_lab12.utils.Result
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import com.example.mobila_applikationer_lab12.utils.getCurrentAreaTemperature

interface WeatherViewModel{
    //VM datamedlemar
    val currentAreaTemperature: StateFlow<String>

    //VM funktioner
    fun fetchWeatherData()
}
//AndroidViewModel erbjuder funktoinalitet som viewModelScope.launch som används i fetch
//WeatherVM implemneterar därför AndroidViewModel
//AndroidViewModel kräver att aplicaton sickas med. Aplicaton är programets state/instans
class WeatherVM(
application: Application
) :AndroidViewModel(application), WeatherViewModel{
    private val _currentAreaTemperature = MutableStateFlow("TEST VALUE")
    override val currentAreaTemperature: StateFlow<String>
        get() = _currentAreaTemperature.asStateFlow()

    private val _weatherState = MutableStateFlow<Result<String>>(Result.Loading)
    val weatherState: StateFlow<Result<String>> = _weatherState

    init {
        getSavedWeather()
    }

    override fun fetchWeatherData() {


        viewModelScope.launch {
            _weatherState.value = Result.Loading
            try {
                val result= WeatherDataSource.getWeather()
                if (result is Result.Success){
//                    _currentAreaTemperature.update { "${result.data}" }
                    val f = Forecast(
                        result.data.approvedTime,
                        result.data.referenceTime,
                        Geometry(
                            result.data.geometry.type,
                            result.data.geometry.coordinates
                        ),
                        result.data.timeSeries
                    )
                    val c = f.getCurrentAreaTemperature()
                    _currentAreaTemperature.update { c.toString() }
                    //TODO SAVE Weather to db
                }else{
                    _weatherState.value = Result.Error(Exception("Faild to fetch data"))
                }
            }catch (e: Exception){
                _weatherState.value = Result.Error(e)
            }
        }
    }

    private fun getSavedWeather(){
        //TODO implement this
    }
}