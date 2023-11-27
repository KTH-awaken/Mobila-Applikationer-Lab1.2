package com.example.mobila_applikationer_lab12

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.navigation.NavHostController
import com.example.mobila_applikationer_lab12.ui.theme.MobilaApplikationerLab12Theme
import androidx.navigation.compose.rememberNavController
import com.example.mobila_applikationer_lab12.ui.screens.Home
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobila_applikationer_lab12.ui.Destinations
import com.example.mobila_applikationer_lab12.ui.components.BottomBar
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import com.example.mobila_applikationer_lab12.model.data.WeatherModel
import com.example.mobila_applikationer_lab12.ui.screens.Favorites
import com.example.mobila_applikationer_lab12.ui.screens.Search
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemBars()
        setContent {
            MobilaApplikationerLab12Theme {
                val navController = rememberNavController()
                val weatherModel = WeatherModel()
                val vm = WeatherVM(application = application, weatherModel = weatherModel)

                runBlocking {
//                    test(weatherModel)
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
            Home(vm)
        }
        composable(Destinations.Search.route) {
            Search(vm = vm,navController)
        }
        composable(Destinations.Favourite.route) {
            Favorites(vm=vm)
        }
    }
}

suspend fun test(weatherModel: WeatherModel) {
    val result1 = weatherModel.getHourlyForecast("Stockholm")
    Log.d("TEST_DATA",result1.toString())
//    val result2 = WeatherDataSource.getWeather()
//    Log.d("TEST_DATA",result2.toString())

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

