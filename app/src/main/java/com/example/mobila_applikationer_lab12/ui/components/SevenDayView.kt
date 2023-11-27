package com.example.mobila_applikationer_lab12.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.mobila_applikationer_lab12.model.data.Day
import com.example.mobila_applikationer_lab12.ui.theme.Styles.componentWidth
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt
import com.example.mobila_applikationer_lab12.R
import com.example.mobila_applikationer_lab12.ui.theme.Styles.lightBlueCard
import com.example.mobila_applikationer_lab12.ui.theme.Styles.lightBlueText
import com.example.mobila_applikationer_lab12.ui.theme.Styles.yellowAccent

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
            containerColor = lightBlueCard,
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
                items(weeklyForecast?.take(30) ?: emptyList()) { day ->
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
    Row(
        modifier = Modifier
            .padding(top = 12.dp, start = 24.dp, end = 24.dp, bottom = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
        //        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = if (isToday) "Idag" else dayName,
            color = if (isToday) lightBlueText else Color.White
        )
        Row(
            modifier = Modifier.weight(1f)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var icon = painterResource(id = R.drawable.drop_half_full)
            val chanceOfRain = day.chanceOfRain?.toDoubleOrNull() ?: 0.0
            if (chanceOfRain>50){
                icon = painterResource(id = R.drawable.drop_full)
            }
            Image(painter = icon, contentDescription = "water droplet")
            Text(
                text = "${chanceOfRain.roundToInt()}%",
                color = Color.White
            )
        }
        Row (
            modifier = Modifier.weight(0.7f)
        ){
            DisplayIcon(icon = day.iconType, size = 30.dp)
        }

        Text(
            text = buildAnnotatedString {
                var highestTemp = day.temperatureHighest.takeIf { it.isNotEmpty() }?.toDoubleOrNull()?.roundToInt()
                var lowestTemp = day.temperatureLowest.takeIf { it.isNotEmpty() }?.toDoubleOrNull()?.roundToInt()

                if (highestTemp == null) {
                    highestTemp=0
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                        append("$highestTemp°")
                    }
                if (lowestTemp == null) {
                    lowestTemp = 0
                }
                withStyle(style = SpanStyle(color = Color.White)) {
                        append("  $lowestTemp°")
                }
            }
        )
    }
}

fun getDayName(date: String): String {
    return SimpleDateFormat("EEEE", Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd").parse(date) ?: Date())
}
fun isToday(date: String): Boolean {
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    return today == date
}