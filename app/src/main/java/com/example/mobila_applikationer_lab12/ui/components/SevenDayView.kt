package com.example.mobila_applikationer_lab12.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import com.example.mobila_applikationer_lab12.model.data.Day
import com.example.mobila_applikationer_lab12.ui.Styles.componentWidth
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun SevenDayView(vm : WeatherVM,){
    val weeklyForecast by vm.weeklyForecast.collectAsState()
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
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp,),
            modifier = Modifier
                .size(width = componentWidth, height = 330.dp)
        ){
            LazyColumn(
                modifier = Modifier
            ){
                items(weeklyForecast?.take(14) ?: emptyList()) { day ->
                    Day(day = day)
                }
            }
        }
    }
}

@Composable
fun Day(day: Day) {
    val dayName = getDayName(day.day)
    val isToday = isToday(day.day)
    Log.d("MyApp",day.toString())
    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = if (isToday) "Idag" else dayName,
            color = if (isToday) Color(242, 230, 113) else Color.White
        )
        Text(text = day.chanceOfRain, color = Color.White)
        Text(text = day.temperatureHighest, color = Color.White)
        Text(text = day.temperatureLowest, color = Color.White)
    }
}

fun getDayName(date: String): String {
    return SimpleDateFormat("EEEE", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd").parse(date) ?: Date())
}
fun isToday(date: String): Boolean {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return today == date
}