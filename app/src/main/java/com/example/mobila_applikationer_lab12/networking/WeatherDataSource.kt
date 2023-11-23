package com.example.mobila_applikationer_lab12.networking

import android.util.Log
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object WeatherDataSource {
    private const val BASE_URL = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/16/lat/58/data.json"
    suspend fun getWeather(): Result<Joke> {
        val urlString = BASE_URL
        val url = URL(urlString)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }

                // Use Gson to parse the JSON string into a Joke object
                val type = object : TypeToken<Joke>() {}.type
                val joke = Gson().fromJson<Joke>(json, type)

                Log.d("TEST", joke.toString())

                Result.Success(joke)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}