package com.example.mobila_applikationer_lab12.ui.viewmodels

import FavouritesRepo
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobila_applikationer_lab12.WeatherApplication
import com.example.mobila_applikationer_lab12.model.data.Day
import com.example.mobila_applikationer_lab12.model.data.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

interface WeatherViewModel{
    //VM funktioner
    suspend fun fetchWeatherData()
}

class WeatherVM(
    application: WeatherApplication,
    private val weatherModel: WeatherModel,
    private val favouritesRepo: FavouritesRepo
    ) :AndroidViewModel(application), WeatherViewModel{


    private val _hourlyForecast = MutableStateFlow<List<Hour>>(emptyList())
    val hourlyForecast: StateFlow<List<Hour>> get() = _hourlyForecast

    private val _weeklyForecast = MutableStateFlow<List<Day>>(emptyList())
    val weeklyForecast: StateFlow<List<Day>> get() = _weeklyForecast

    private var _cityToShow = MutableStateFlow<String>("Stockholm")
    val cityToShow: StateFlow<String> get() = _cityToShow

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> get() = _favorites

    private var _isFavorite = MutableStateFlow<Boolean>(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite


    init {
        viewModelScope.launch(Dispatchers.Default) {
            if (isInternetAvailable(context = application)) {
                withContext(Dispatchers.IO) {
                    fetchWeatherData()
                    saveHomeData()
                }
                Log.d("INTERNET", "Has internet")
            } else {
                Log.d("INTERNET", "Has no internet")
                favouritesRepo.homeData.collect { homeData ->
                    _hourlyForecast.value = homeData.hourly
                    _weeklyForecast.value = homeData.weekly
                    Log.d("COLLECT", "Home data collection: ${_hourlyForecast.value}")
                }
            }
        }
        viewModelScope.launch {
            Log.d("COLLECT","Collecting...")
            favouritesRepo.favorites.collect{
                _favorites.value = it
                Log.d("COLLECT","Collect: ${_favorites.value}")
            }
            Log.d("COLLECT","Favourites collection: ${_favorites.value}")
        }
    }

    override suspend fun fetchWeatherData() {
        //Hourly
        updateHourlyForecast()
        //Weekly
        updateWeeklyForecast()


    }


    private fun updateHourlyForecast() {
        val rawForecast = weatherModel.getHourlyForecast(cityToShow.value)
        val hourList = rawForecast.map { timeSeries ->
            Hour(
                time = timeSeries.validTime,
                temperature = "${timeSeries.parameters.find { it.name == "t" }?.values?.get(0)}",
                chanceOfRain = "${timeSeries.parameters.find { it.name == "pcat" }?.values?.get(0)}",
                iconType = weatherModel.determineIconType(timeSeries.parameters)
            )
        }
        _hourlyForecast.value = hourList
    }

    private suspend fun updateWeeklyForecast(){
        val forecast = weatherModel.getWeeklyForecast(cityToShow.value)
        _weeklyForecast.value = forecast
    }

    fun setCityToShow(cityToShow:String){
        _cityToShow.value=cityToShow
    }

    fun addToFavorites(cityName: String, day:Day){
        Log.d("ADDING","CityName= $cityName")
        val currentFavourites = _favorites.value.toMutableList()
        currentFavourites.add(Favorite(cityName, day))
        _favorites.value = currentFavourites
        Log.d("SAVING","Adding Favourites : ${_favorites.value}")
        saveFavouritesData()
    }
    fun removeFromFavorites(cityName: String){
        //todo implement
    }

    fun isCityFavorite(cityName: String):Boolean{
        return favorites.value.any { it.cityName == cityName }
    }

    fun setIsFavorite(isFavorite: Boolean){
        _isFavorite.value=isFavorite
    }

    private fun saveFavouritesData(){
        runBlocking {
            Log.d("SAVING","Saving favourites=${_favorites.value}")
            favouritesRepo.saveFavorites(_favorites.value)
        }
    }
    private fun saveHomeData(){
        runBlocking {
            Log.d("SAVING","Saving home data; hourly data=${_hourlyForecast.value}")
            Log.d("SAVING","Saving home data; weekly data=${_weeklyForecast.value}")
            favouritesRepo.saveHomeData(HomeData(_weeklyForecast.value,_hourlyForecast.value))
        }
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
    fun isInternetAvailable():Boolean{
        return isInternetAvailable(this.getApplication())
    }

}

data class Hour(
    val time: String,
    val temperature: String,
    val chanceOfRain: String,
    val iconType: String
)

data class Favorite(
    val cityName:String,
    val day: Day
)
data class HomeData(
    val weekly: List<Day>,
    val hourly:List<Hour>
)

