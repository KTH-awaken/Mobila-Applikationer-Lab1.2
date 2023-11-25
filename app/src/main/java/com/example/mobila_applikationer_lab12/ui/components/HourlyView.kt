package com.example.mobila_applikationer_lab12.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mobila_applikationer_lab12.ui.Styles.componentWidth
import com.example.mobila_applikationer_lab12.ui.viewmodels.Hour
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM

@Composable
fun HourlyView(vm : WeatherVM, ) {
    val hourlyForecast by vm.hourlyForecast.collectAsState()
    Column(
        modifier = Modifier
            .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val customCardColors = CardDefaults.cardColors(
            containerColor = Color(54, 59, 100),
        )
        ElevatedCard(
            colors = customCardColors,
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .size(width = componentWidth, height = 130.dp)
        ){
//            val visibleHours = vm.hourlyForecast.value.take(5)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
//                visibleHours.forEach { hour ->
                   Hour(hour = hourlyForecast[0])
//                }
            }
        }
    }
}

@Composable
fun Hour(hour: Hour){
    Column(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 12.dp)
            .fillMaxHeight()
        ,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
//        Text(text = hour.time,color = Color.White)
        Text(text = hour.temperature+"7°",color = Color.White)
        Text(text = hour.chanceOfRain+"%",color = Color.White)
    }
}