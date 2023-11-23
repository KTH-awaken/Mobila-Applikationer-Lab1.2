package com.example.mobila_applikationer_lab12.networking

import android.util.Log
import com.example.mobila_applikationer_lab12.model.data.Joke
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import com.example.mobila_applikationer_lab12.utils.Result

object JokeDataSource {
    private const val BASE_URL = "https://v2.jokeapi.dev/joke/Any"

    suspend fun getRandomJoke(): Result<Joke> {
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