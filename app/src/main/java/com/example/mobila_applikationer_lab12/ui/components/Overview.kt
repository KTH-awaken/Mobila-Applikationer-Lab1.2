package com.example.mobila_applikationer_lab12.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobila_applikationer_lab12.R
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlin.math.roundToInt

@Composable
fun Overview(
   vm :WeatherVM
){
    val cityToShow = vm.cityToShow.collectAsState()
    val hourlyForecast by vm.hourlyForecast.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .padding(top = 40.dp,bottom = 30.dp),
            text = "${hourlyForecast[0].temperature.toDoubleOrNull()?.roundToInt()}°",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 60.sp),
            color = Color.White,
        )
        DisplayIcon(icon = hourlyForecast[0].iconType, size =64.dp )
        Text(
            modifier = Modifier
                .padding(bottom = 15.dp),
            text = "${cityToShow.value}",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
        )
    }
}

@Composable
fun WeatherIcon(
    iconResId: Int
){
    val icon = painterResource(id = iconResId)
    Image(
        painter = icon,
        contentDescription = null,
        modifier = Modifier.size(64.dp)
    )
}