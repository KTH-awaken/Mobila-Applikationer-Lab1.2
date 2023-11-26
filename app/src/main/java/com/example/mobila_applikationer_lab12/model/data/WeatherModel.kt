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
        val highestTemp = timeSeries.flatMap { it.parameters[ParameterIndex.T.ordinal].values }
            .max()
        val lowestTemp = timeSeries.flatMap { it.parameters[ParameterIndex.T.ordinal].values }
            .min()
        return DayForecast(timeSeries,highestTemp,lowestTemp)
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
                val highestTemp = dailyTimeSeries.flatMap { it.parameters[ParameterIndex.T.ordinal].values }.max()
                val lowestTemp = dailyTimeSeries.flatMap { it.parameters[ParameterIndex.T.ordinal].values }.min()
                val averageWindSpeed = dailyTimeSeries.flatMap { it.parameters[ParameterIndex.WS.ordinal].values }
                    .average()
                forecastList.add(DayForecast(dailyTimeSeries, highestTemp, lowestTemp))
            }
        }
        return forecastList
    }

}
enum class ParameterIndex {
    SPP,
    PCAT,
    PMIN,
    PMEAN,
    PMAX,
    PMEDIAN,
    TCC_MEAN,
    LCC_MEAN,
    MCC_MEAN,
    HCC_MEAN,
    T,
    MSL,
    VIS,
    WD,
    WS,
    R,
    TSTM,
    GUST,
    WSYMB2
}
data class DayForecast(
    val timeSeries: List<TimeSeries>,
    val highestTemp: Double,
    val lowestTemp: Double,
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
