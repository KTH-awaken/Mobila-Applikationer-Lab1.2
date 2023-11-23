package com.example.mobila_applikationer_lab12.networking

import android.util.Log
import com.example.mobila_applikationer_lab12.model.data.Forecast
import com.example.mobila_applikationer_lab12.model.data.Place
import com.example.mobila_applikationer_lab12.utils.Result
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import java.net.HttpURLConnection
import java.net.URL
@Serializable
object PlaceDataSource{
    private const val BASE_URL = "https://geocode.maps.co/search?q="
    suspend fun getPlace(placeName:String): Result<Place> {
        val urlString = BASE_URL + placeName
        val url = URL(urlString)

        return withContext(Dispatchers.IO) {
            try {
                val connection = url.openConnection() as HttpURLConnection
                val inputStream = connection.inputStream
                val json = inputStream.bufferedReader().use { it.readText() }
                Log.d("TEST","Input stream of place: "+  json.toString())
                val type = object : TypeToken<List<Place>>() {}.type
                val places = Gson().fromJson<List<Place>>(json, type)
                if (places.isNotEmpty()) {
                    val firstPlace = places[0]
                    Result.Success(firstPlace)
                } else {
                    throw Exception()
                }
            } catch (e: Exception) {
                Log.d("TEST_ERROR", e.toString())
                Result.Error(e)
            }
        }
    }
}