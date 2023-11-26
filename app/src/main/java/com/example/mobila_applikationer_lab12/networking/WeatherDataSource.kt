package com.example.mobila_applikationer_lab12.networking

import android.util.Log
import com.example.mobila_applikationer_lab12.model.data.Forecast
import com.example.mobila_applikationer_lab12.model.data.Geometry
import com.example.mobila_applikationer_lab12.model.data.Parameter
import com.example.mobila_applikationer_lab12.model.data.Place
import com.example.mobila_applikationer_lab12.model.data.TimeSeries
import com.example.mobila_applikationer_lab12.model.data.Weather
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import com.example.mobila_applikationer_lab12.utils.Result
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException

object WeatherDataSource {

    suspend fun getWeather(longitude:Double,latitude:Double): Result<Forecast> {
        val lon = truncateLastCharsFromDouble(longitude,2)
        val lat = truncateLastCharsFromDouble(latitude,2)

        return try {
            val forecast = RetrofitInstance.weatherService.getWeather(lon, lat)
            Result.Success(forecast)
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    suspend fun getWeather(place : Place):Result<Forecast>{
        return getWeather(place.lon,place.lat)
    }
    suspend fun getWeather():Result<Forecast>{
        return getWeather(18.07109,59.32511)
    }
    private fun truncateLastCharsFromDouble(value:Double,nrOfCharacters:Int): Double {
        val lonString = value.toString()
        val truncatedLonString = lonString.dropLast(nrOfCharacters)
        return truncatedLonString.toDouble()
    }
    private fun parseData(json: String): Forecast {
        val jsonObject = JSONObject(json)

        val approvedTime = jsonObject.getString("approvedTime")
        val referenceTime = jsonObject.getString("referenceTime")

        val geometryObject = jsonObject.getJSONObject("geometry")
        val geometryType = geometryObject.getString("type")
        val coordinatesArray = geometryObject.getJSONArray("coordinates")
        val coordinates = List(coordinatesArray.length()) { index ->
            val innerArray = coordinatesArray.getJSONArray(index)
            val innerCoordinates = List(innerArray.length()) { innerIndex ->
                innerArray.getDouble(innerIndex)
            }
            innerCoordinates
        }

        val timeSeriesArray = jsonObject.getJSONArray("timeSeries")
        val timeSeries = List(timeSeriesArray.length()) { index ->
            val seriesObject = timeSeriesArray.getJSONObject(index)
            val validTime = seriesObject.getString("validTime")

            val parametersArray = seriesObject.getJSONArray("parameters")
            val parameters = List(parametersArray.length()) { paramIndex ->
                val parameterObject = parametersArray.getJSONObject(paramIndex)
                val paramName = parameterObject.getString("name")
                val levelType = parameterObject.getString("levelType")
                val level = parameterObject.getInt("level")
                val unit = parameterObject.getString("unit")

                val valuesArray = parameterObject.getJSONArray("values")
                val values = List(valuesArray.length()) { valuesArray.getDouble(it) }

                Parameter(paramName, levelType, level, unit, values)
            }
            TimeSeries(validTime, parameters)
        }
        return Forecast(approvedTime, referenceTime, Geometry(geometryType, coordinates), timeSeries)
    }


}