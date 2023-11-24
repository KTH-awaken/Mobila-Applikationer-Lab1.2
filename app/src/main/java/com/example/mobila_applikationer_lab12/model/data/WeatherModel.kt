package com.example.mobila_applikationer_lab12.model.data
import android.util.Log
import com.example.mobila_applikationer_lab12.networking.PlaceDataSource
import com.example.mobila_applikationer_lab12.networking.WeatherDataSource
import com.example.mobila_applikationer_lab12.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


interface IWeatherModel{

}

class WeatherModel:IWeatherModel{
    suspend fun getPlace(placeName: String):Place? {
        var place:Place? = null
        when (val result = PlaceDataSource.getPlace(placeName)) {
            is Result.Success -> {
                place = result.data
            }
            else -> {
                Log.d("ERROR", "Error getting place")
            }
        }
        return place
    }
    suspend fun getForecast(place : Place):Forecast?{
        var forecast:Forecast? = null
        when (val result = WeatherDataSource.getWeather(place)) {
            is Result.Success -> {
                forecast = result.data
            }
            else -> {
                Log.d("ERROR", "Error getting forecast")
            }
        }
        return forecast
    }

    private suspend fun getRawForecastByPlace(placeName: String): Forecast {
        val place = getPlace(placeName) ?: throw Exception("Could not find place")
        return getForecast(place) ?: throw Exception("Could not find forecast")
    }
    /*
    fun getForecastByPlace(placeName: String): Forecast {
        try{
            return getRawForecastByPlace(placeName)
        }catch (e:Exception){
            Log.d("ERROR",e.toString())
        }
    }

     */


    fun getHourlyForecast(placeName: String):List<TimeSeries>{
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return runBlocking {
            val parametersList = async(Dispatchers.IO) {
                val rawForecast = getRawForecastByPlace(placeName)
                rawForecast?.timeSeries
                    ?.filter { it.validTime.startsWith(currentDate)}
                    ?.map {
                        Log.d("DATA_HOURLY","Valid time: "+ it.validTime.toString())
                        TimeSeries(it.validTime, it.parameters) }
                    ?: emptyList()
            }
            parametersList.await()
        }
    }
    fun getWeeklyForecast(placeName:String){

    }

}



data class Weather(
    val error: Boolean,
    val tmp: String,
)
data class Forecast(
    val approvedTime: String,
    val referenceTime: String,
    val geometry: Geometry,
    val timeSeries: List<TimeSeries>
)

data class Geometry(
    val type: String,
    val coordinates: List<List<Double>>
)

data class TimeSeries(
    val validTime: String,
    val parameters: List<Parameter>
)

data class Parameter(
    val name: String,
    val levelType: String,
    val level: Int,
    val unit: String,
    val values: List<Double>
)
