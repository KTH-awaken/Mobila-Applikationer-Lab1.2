package com.example.mobila_applikationer_lab12.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mobila_applikationer_lab12.R
import com.example.mobila_applikationer_lab12.ui.theme.Styles.componentWidth
import com.example.mobila_applikationer_lab12.ui.theme.Styles.lightBlueCard
import com.example.mobila_applikationer_lab12.ui.theme.Styles.yellowAccent
import com.example.mobila_applikationer_lab12.ui.viewmodels.Hour
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

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
            containerColor = lightBlueCard,
        )

        ElevatedCard(
            colors = customCardColors,
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .size(width = componentWidth, height = 140.dp)
        ){

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LazyRow(
                    modifier = Modifier
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    items(hourlyForecast?.take(12) ?: emptyList()) { hour ->
                        Hour(hour = hour)
                    }
                }
            }
        }
    }
}

@Composable
fun Hour(hour: Hour){
    Column(
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp)
            .fillMaxHeight()
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val parsedDateTime = LocalDateTime.parse(hour.time, originalFormatter)
        val currentDateTime = LocalDateTime.now()

        if (parsedDateTime.hour == currentDateTime.hour) {
            Text(text = "Nu", color = yellowAccent)
        } else {
            Text(text = DateTimeFormatter.ofPattern("HH:mm").format(parsedDateTime), color = Color.White)
        }
        DisplayIcon(icon = hour.iconType, size = 30.dp)
        Text(text = (hour.temperature.toDoubleOrNull()?.roundToInt()?.toString() + "°") ?: "", color = Color.White)
        Row {
            var icon = painterResource(id = R.drawable.drop_half_full)
            val chanceOfRain = hour.chanceOfRain?.toDoubleOrNull() ?: 0.0
            if (chanceOfRain >50){
                icon = painterResource(id = R.drawable.drop_full)
            }
            Image(painter = icon, contentDescription = "water droplet")
        Text(text = (hour.chanceOfRain.toDoubleOrNull()?.roundToInt()?.toString().toString()+"%"),color = Color.White)
        }
    }
}