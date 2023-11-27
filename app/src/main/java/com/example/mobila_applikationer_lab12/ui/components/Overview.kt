package com.example.mobila_applikationer_lab12.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobila_applikationer_lab12.R
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.mobila_applikationer_lab12.ui.theme.Styles.blueBg
import com.example.mobila_applikationer_lab12.ui.theme.Styles.yellowAccent
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
                .padding(top = 20.dp,bottom = 20.dp),
            text = "${hourlyForecast[0].temperature.toDoubleOrNull()?.roundToInt()}°",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 60.sp),
            color = Color.White,
        )

        DisplayIcon(icon = hourlyForecast[0].iconType, size =64.dp )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
           Row(
            horizontalArrangement = Arrangement.Center
           ){
               AddToFavorite(vm = vm, modifier = Modifier, blueBg,false)
               Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "${cityToShow.value}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                )
           }
            Spacer(modifier = Modifier.width(2.dp))
            AddToFavorite(vm = vm,modifier =Modifier.padding(bottom = 5.dp), yellowAccent,true)
        }
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