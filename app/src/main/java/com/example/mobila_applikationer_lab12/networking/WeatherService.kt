package com.example.mobila_applikationer_lab12.networking
import com.example.mobila_applikationer_lab12.model.data.Forecast
import retrofit2.http.GET
import retrofit2.http.Path


interface WeatherService {
    @GET("api/category/pmp3g/version/2/geotype/point/lon/{lon}/lat/{lat}/data.json")
    suspend fun getWeather(@Path("lon") lon: Double, @Path("lat") lat: Double): Forecast
}