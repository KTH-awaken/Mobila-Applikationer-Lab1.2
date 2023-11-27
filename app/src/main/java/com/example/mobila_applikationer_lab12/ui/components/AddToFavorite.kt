package com.example.mobila_applikationer_lab12.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import com.example.mobila_applikationer_lab12.ui.theme.Styles.yellowAccent
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun AddToFavorite(
    vm: WeatherVM,
    modifier: Modifier = Modifier,
    color: Color,
    show: Boolean,
) {
    val cityToShow = vm.cityToShow.collectAsState()
    val weeklyForecast by vm.weeklyForecast.collectAsState()


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (vm.isCityFavorite(cityToShow.value)) {
                    vm.removeFromFavorites(cityToShow.value)
                } else {
                    vm.addToFavorites(cityToShow.value,weeklyForecast[0])
                }
            }
        ) {
            if(show){
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Star",
                    tint = if (vm.isCityFavorite(cityToShow.value)) color else Color.Gray
                )
            }else{
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Star",
                    tint =color
                )
            }
        }
    }
}

