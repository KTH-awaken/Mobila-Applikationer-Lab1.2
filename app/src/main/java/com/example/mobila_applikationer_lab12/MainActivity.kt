package com.example.mobila_applikationer_lab12

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobila_applikationer_lab12.networking.JokeDataSource
import com.example.mobila_applikationer_lab12.ui.theme.MobilaApplikationerLab12Theme
import kotlinx.coroutines.runBlocking
import androidx.navigation.compose.rememberNavController
import com.example.mobila_applikationer_lab12.ui.screens.Home
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobila_applikationer_lab12.networking.WeatherDataSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobilaApplikationerLab12Theme {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController, startDestination = "home"){
                        composable("home"){
                            Home()
                        }
                    }
                    runBlocking {
                        test()
                    }
                }
            }
        }
    }
}

suspend fun test(){
    Log.d("TEST","Hello world")
    val result = JokeDataSource.getRandomJoke()
    Log.d("TEST",result.toString())

    val result2 = WeatherDataSource.getWeather()
    Log.d("TEST",result2.toString())
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