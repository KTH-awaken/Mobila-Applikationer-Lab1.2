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
import java.util.Calendar
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
    //Nya getHourlyFrocast den enda skilanden är att den hämtar alltid 12 timmar framåt oavset hur nära kl är 12
    fun getHourlyForecast(placeName: String): List<TimeSeries> {
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())
        val twelveHoursLater = Calendar.getInstance()
        twelveHoursLater.add(Calendar.HOUR_OF_DAY, 12)
        val twelveHoursLaterDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(twelveHoursLater.time)

        return runBlocking {
            val parametersList = async(Dispatchers.IO) {
                val rawForecast = getRawForecastByPlace(placeName)
                rawForecast?.timeSeries
                    ?.filter { it.validTime >= currentDateTime && it.validTime <= twelveHoursLaterDateTime }
                    ?.map {
                        Log.d("DATA_HOURLY", "Valid time: " + it.validTime.toString())
                        TimeSeries(it.validTime, it.parameters)
                    }
                    ?: emptyList()
            }
            parametersList.await()
        }
    }

    suspend fun getWeeklyForecast(placeName: String): List<Day> {
        val numberOfDays = 14
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.DAY_OF_YEAR, numberOfDays)
        val endDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(endDate.time)

        return runBlocking {
            val parametersList = async(Dispatchers.IO) {
                val rawForecast = getRawForecastByPlace(placeName)
                rawForecast?.timeSeries
                    ?.filter { it.validTime >= currentDateTime && it.validTime <= endDateTime }
                    ?.groupBy { it.validTime.substring(0, 10) } // Group by date
                    ?.map { (date, timeSeriesList) ->
                        val dayForecast = timeSeriesList.firstOrNull()
                        val highestTemp = dayForecast?.parameters?.find { it.name == "t" }?.values?.maxOrNull()?.toString()
                        val lowestTemp = dayForecast?.parameters?.find { it.name == "t" }?.values?.minOrNull()?.toString()
                        val chanceOfRain = dayForecast?.parameters?.find { it.name == "r" }?.values?.average()?.toString()

                        Day(date, highestTemp ?: "", lowestTemp ?: "", chanceOfRain ?: "")
                    }
                    ?: emptyList()
            }
            parametersList.await()
        }
    }
}

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
data class Day(
    val day: String,
    val temperatureHighest: String,
    val temperatureLowest: String,
    val chanceOfRain: String
)