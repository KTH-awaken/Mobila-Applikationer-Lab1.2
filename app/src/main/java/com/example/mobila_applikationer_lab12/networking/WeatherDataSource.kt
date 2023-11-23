package com.example.mobila_applikationer_lab12.networking

import android.util.Log
import com.example.mobila_applikationer_lab12.model.data.Forecast
import com.example.mobila_applikationer_lab12.model.data.Geometry
import com.example.mobila_applikationer_lab12.model.data.Parameter
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

object WeatherDataSource {
    private const val BASE_URL = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/16/lat/58/data.json"
    suspend fun getWeather(): Result<Forecast> {
        val urlString = BASE_URL
        val url = URL(urlString)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }
                Log.d("TEST","Input stream: "+  json.toString())
                val forecast = parseData(json)
                Result.Success(forecast)
            } catch (e: Exception) {
                Log.d("TEST_ERROR", e.toString())
                Result.Error(e)
            }
        }
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