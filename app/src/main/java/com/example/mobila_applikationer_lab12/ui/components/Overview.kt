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


@Composable
fun Overview(
    //todo add vm
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .padding(top = 40.dp,bottom = 40.dp),
            text = "7°",
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 60.sp),
            color = Color.White,
        )
        WeatherIcon(iconResId = R.drawable.baseline_nights_stay_24)
        Text(
            modifier = Modifier
                .padding(bottom = 10.dp),
            text = "Stockholm",
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