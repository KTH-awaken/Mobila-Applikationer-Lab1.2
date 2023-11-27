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
import kotlin.math.log


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
//        Log.d("myapp3",forecast.toString())
        return forecast //real
//        return getTestForecast()//todo remove test
    }




    private suspend fun getRawForecastByPlace(placeName: String): Forecast {
        val place = getPlace(placeName) ?: throw Exception("Could not find place")//real
//        Log.d("myApp3",place.toString())
        return getForecast(place) ?: throw Exception("Could not find forecast")
//        return getForecast(getTestPlace()) ?: throw Exception("Could not find forecast")//test
    }


    fun getTestPlace(): Place {
        return Place(
            place_id = 1,
            licence = "Test Licence",
            powered_by = "Test Provider",
            osm_type = "node",
            osm_id = 12345,
            boundingbox = listOf(0.0, 1.0, 2.0, 3.0),
            lat = 59.32511,  // Replace with your desired latitude
            lon = 18.07109,  // Replace with your desired longitude
            display_name = "Test City",
            `class` = "place",
            type = "city",
            importance = 0.9
        )
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

    fun getHourlyForecast(placeName: String): List<TimeSeries> {
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(Date())
        val twelveHoursLater = Calendar.getInstance()
        twelveHoursLater.add(Calendar.HOUR_OF_DAY, 12)
        val twelveHoursLaterDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(twelveHoursLater.time)

        return runBlocking {
            val parametersList = async(Dispatchers.IO) {
                val rawForecast = getRawForecastByPlace(placeName)
                rawForecast?.timeSeries
                    ?.filter {
                        val oneHourAgo = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) }
                        val oneHourAgoDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(oneHourAgo.time)

                        it.validTime >= oneHourAgoDateTime && it.validTime <= twelveHoursLaterDateTime
                    }
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
    val numberOfDays = 30
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
                    val highestTemp = timeSeriesList.flatMap { it.parameters }
                        .filter { it.name == "t" }
                        .flatMap { it.values }
                        .maxOrNull()?.toString() ?: ""
                    val lowestTemp = timeSeriesList.flatMap { it.parameters }
                        .filter { it.name == "t" }
                        .flatMap { it.values }
                        .minOrNull()?.toString() ?: ""
                    val chanceOfRain = timeSeriesList.flatMap { it.parameters }
                        .find { it.name == "r" }?.values?.average()?.toString() ?: ""

                    // Determine the iconType based on conditions
                    val iconType = determineIconType(timeSeriesList.flatMap { it.parameters })

                    Day(date, highestTemp, lowestTemp, chanceOfRain, iconType)
                }
                ?: emptyList()
        }
        parametersList.await()
    }
}
     fun determineIconType(parameters: List<Parameter>): String {
        val precipitationCategory = parameters.find { it.name == "pcat" }?.values?.firstOrNull() ?: 0.0
        val snowfall = parameters.find { it.name == "pmean" }?.values?.firstOrNull() ?: 0.0
        val cloudCoverHigh = parameters.find { it.name == "hcc_mean" }?.values?.firstOrNull() ?: 0.0
        val totalCloudCover = parameters.find { it.name == "tcc_mean" }?.values?.firstOrNull() ?: 0.0

        return when {
            precipitationCategory > 0.0 && snowfall > 0.0 -> "snowy"
            precipitationCategory > 0.0 -> "rain"
            totalCloudCover < 2.0 -> "sunny"
            cloudCoverHigh > 7.0 -> "cloudy"
            else -> "default"
        }
    }
    private fun getTestForecast(): Forecast {//todo remove test
        return Forecast(
            approvedTime = "2023-11-26T12:08:03Z",
            referenceTime = "2023-11-26T12:00:00Z",
            geometry = Geometry(
                type = "Point",
                coordinates = listOf(listOf(18.087151, 59.314249))
            ),
            timeSeries = listOf(
                TimeSeries(
                    validTime = "2023-11-26T13:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T14:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                TimeSeries(
                    validTime = "2023-11-26T15:00:00Z",
                    parameters = listOf(
                        Parameter(name = "spp", levelType = "hl", level = 1, unit = "percent", values = listOf(-9.0)),
                        // Add other parameters here
                    )
                ),
                // Add more TimeSeries as needed
            )
        )
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
    val chanceOfRain: String,
    val iconType: String
)