package com.example.mobila_applikationer_lab12

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mobila_applikationer_lab12.networking.JokeDataSource
import com.example.mobila_applikationer_lab12.ui.theme.MobilaApplikationerLab12Theme
import kotlinx.coroutines.runBlocking
import androidx.navigation.compose.rememberNavController
import com.example.mobila_applikationer_lab12.ui.screens.Home
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobila_applikationer_lab12.networking.WeatherDataSource
import com.example.mobila_applikationer_lab12.ui.Destinations
import com.example.mobila_applikationer_lab12.ui.components.BottomBar
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import com.example.mobila_applikationer_lab12.model.data.DayForecast
import com.example.mobila_applikationer_lab12.model.data.WeatherModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemBars()
        setContent {
            MobilaApplikationerLab12Theme {
                val navController = rememberNavController()
                val vm = WeatherVM(application = application)

                    runBlocking {
                        test()
                    }

                var buttonsVisible = remember { mutableStateOf(true) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = { BottomBar(navController = navController, state =buttonsVisible) }
                    ){paddingValues ->
                        Box(
                            modifier =Modifier.padding(paddingValues)
                        ){
                            NavigationGraph(navController = navController, vm = vm )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController,vm: WeatherVM) {
    NavHost(navController, startDestination = Destinations.HomeScreen.route) {
        composable(Destinations.HomeScreen.route) {
            Home(vm,navController)
        }
        composable(Destinations.Search.route) {
        }
        composable(Destinations.Favourite.route) {
        }
    }
}

suspend fun test(){

    val result2 = WeatherDataSource.getWeather()
    Log.d("TEST_DATA",result2.toString())

    Log.d("TEST","Hello world")
    val result = JokeDataSource.getRandomJoke()
    Log.d("TEST",result.toString())

    val weatherModel = WeatherModel()
    val dayForecast = weatherModel.getHourlyForecast("Stockholm")
    Log.d("DATA_DAY","Current time=${dayForecast.timeSeries[1].validTime}")
    Log.d("DATA_DAY","Lowest tmp=${dayForecast.lowestTemp} highest tmp=${dayForecast.highestTemp} Avg Wind Speed=${dayForecast.averageWindSpeed}")
    val weeklyForecast = weatherModel.getWeeklyForecast("Stockholm")
    Log.d("DATA_WEEKLY","Nr of weekly forecasts=${weeklyForecast.size}")
    weeklyForecast.forEach {
        Log.d("DATA_WEEKLY_DAY","Lowest tmp=${it.lowestTemp} Highest tmp=${it.highestTemp} Avg Wind Speed=${it.averageWindSpeed} avg Precipitation=${it.avgPCAT}")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobilaApplikationerLab12Theme {
        Greeting("Android")
    }
}

fun ComponentActivity.hideSystemBars() {
    window.decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LOW_PROFILE
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.let {
            it.hide(WindowInsets.Type.statusBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}

