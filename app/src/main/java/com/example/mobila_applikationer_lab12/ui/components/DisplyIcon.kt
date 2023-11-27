package com.example.mobila_applikationer_lab12.ui.components
import com.example.mobila_applikationer_lab12.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun DisplayIcon(icon:String,size: Dp){
    val drawableId = when (icon){
        "snowy" -> R.drawable.snowy
        "rain" -> R.drawable.rain
        "sunny" -> R.drawable.sunny
        "cloudy" -> R.drawable.cloudy
        else -> R.drawable.cloudy
    }
    val iconPainter = painterResource(id = drawableId)
    Image(
        painter = iconPainter,
        contentDescription = "Weather Icon",
        modifier = Modifier.size(size),
    )
}