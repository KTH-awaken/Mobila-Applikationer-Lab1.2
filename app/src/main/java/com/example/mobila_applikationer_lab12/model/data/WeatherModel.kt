package com.example.mobila_applikationer_lab12.model.data
import android.util.Log
import com.example.mobila_applikationer_lab12.networking.PlaceDataSource
import com.example.mobila_applikationer_lab12.networking.WeatherDataSource
import com.example.mobila_applikationer_lab12.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.ceil


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


    fun getHourlyForecast(placeName: String):DayForecast{
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val timeSeries = runBlocking {
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
        val highestTemp = timeSeries.flatMap { it.parameters[findIndexByParameterName("t",it)].values }
            .max()
        val lowestTemp = timeSeries.flatMap { it.parameters[findIndexByParameterName("t",it)].values }
            .min()
        val averageWindSpeed = timeSeries.flatMap { it.parameters[findIndexByParameterName("ws",it)].values }
            .average()
        val pcat = timeSeries.flatMap { it.parameters[findIndexByParameterName("pcat",it)].values }

        val occurrences = pcat.groupingBy { it }.eachCount()
        val mostOccurringNumber = occurrences.maxByOrNull { it.value }?.key
        var prepicitation_cat = "No precipitation"
        if( mostOccurringNumber!= null){
            prepicitation_cat = findPrecipitationByIndex(mostOccurringNumber.toInt())
        }

        return DayForecast(timeSeries,highestTemp,lowestTemp,String.format("%.1f",averageWindSpeed).toDouble(),prepicitation_cat)
    }
    fun getWeeklyForecast(placeName: String): List<DayForecast> {
        val rawForecast = runBlocking(Dispatchers.IO) {
            getRawForecastByPlace(placeName)
        } ?: return emptyList()
        Log.d("TEST_WEEKLY","Size of time serise in rawForecast=${rawForecast.timeSeries.size}")
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val timeSeriesList = rawForecast.timeSeries

        val forecastList = mutableListOf<DayForecast>()

        for (i in 0 until 7) {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(currentDate)
            val nextDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date(currentDate.time + TimeUnit.DAYS.toMillis(i.toLong())))

            val dailyTimeSeries = timeSeriesList.filter { it.validTime.startsWith(nextDate) }
            if (dailyTimeSeries.isNotEmpty()) {
                val highestTemp = dailyTimeSeries.flatMap { it.parameters[findIndexByParameterName("t",it)].values }.max()
                val lowestTemp = dailyTimeSeries.flatMap { it.parameters[findIndexByParameterName("t",it)].values }.min()
                val averageWindSpeed = dailyTimeSeries.flatMap { it.parameters[findIndexByParameterName("ws",it)].values }
                    .average()
                val pcat = dailyTimeSeries.flatMap { it.parameters[findIndexByParameterName("pcat",it)].values }

                val occurrences = pcat.groupingBy { it }.eachCount()
                val mostOccurringNumber = occurrences.maxByOrNull { it.value }?.key
                var prepicitation_cat = "No precipitation"
                if( mostOccurringNumber!= null){
                    prepicitation_cat = findPrecipitationByIndex(mostOccurringNumber.toInt())
                }
                forecastList.add(DayForecast(
                    dailyTimeSeries,
                    highestTemp,
                    lowestTemp,
                    String.format("%.1f",averageWindSpeed).toDouble(),
                    prepicitation_cat))
            }
        }
        return forecastList
    }
    fun findPrecipitationByTimeSeries(timeSeries: TimeSeries):String{
        val index = timeSeries.parameters[findIndexByParameterName("pcat",timeSeries)].values[0]
        return findPrecipitationByIndex(index.toInt())
    }
    fun findTemperatureByTimeSeries(timeSeries: TimeSeries):Double{
        return timeSeries.parameters[findIndexByParameterName("t",timeSeries)].values[0]
    }
    fun findWindSpeedByTimeSeries(timeSeries: TimeSeries):Double{
        return timeSeries.parameters[findIndexByParameterName("ws",timeSeries)].values[0]
    }
    private fun findPrecipitationByIndex(i:Int):String{
        return when(i){
            0 -> "No precipitation"
            1 -> "Snow"
            2 -> "Snow and rain"
            3 -> "Rain"
            4 -> "Drizzle"
            5 -> "Freezing rain"
            6 -> "Freezing drizzle"
            else -> "Unknown precipitation"
        }
    }

    private fun findIndexByParameterName(name:String, timeSeries: TimeSeries):Int{
        return timeSeries.parameters.indexOfFirst { it.name == name }
    }

}

data class DayForecast(
    val timeSeries: List<TimeSeries>,
    val highestTemp: Double,
    val lowestTemp: Double,
    val averageWindSpeed:Double,
    val avgPCAT:String
)

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
