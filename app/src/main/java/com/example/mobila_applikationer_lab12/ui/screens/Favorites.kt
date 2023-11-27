package com.example.mobila_applikationer_lab12.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.mobila_applikationer_lab12.ui.components.DisplayIcon
import com.example.mobila_applikationer_lab12.ui.theme.Styles
import com.example.mobila_applikationer_lab12.ui.theme.Styles.lightBlueCard
import com.example.mobila_applikationer_lab12.ui.viewmodels.Favorite
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import kotlin.math.roundToInt

@Composable
fun Favorites(
    vm :WeatherVM
){
    val favorites by vm.favorites.collectAsState()
    Column( // Container
        modifier = Modifier
            .fillMaxSize()
            .background(Styles.blueBg),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){
        favorites.forEach { favorite ->
            Favorite(favorite)
        }
    }
}

@Composable
fun Favorite(favorite:Favorite){

    val customCardColors = CardDefaults.cardColors(
        containerColor = lightBlueCard,
    )
    ElevatedCard(
        colors = customCardColors,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .size(width = Styles.componentWidth, height = 140.dp)
            .padding(top = 10.dp, bottom = 10.dp)
    ){


        Row(

        ){
            Column(
                modifier = Modifier.fillMaxWidth(0.5f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = "${favorite.cityName}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,

                )

                var highestTemp = favorite.day.temperatureHighest.takeIf { it.isNotEmpty() }?.toDoubleOrNull()?.roundToInt()
                var lowestTemp = favorite.day.temperatureLowest.takeIf { it.isNotEmpty() }?.toDoubleOrNull()?.roundToInt()
                if (highestTemp == null) { highestTemp=0}
                if (lowestTemp == null) { lowestTemp = 0}

                Text(text = "High  $highestTemp°", color = Color.White, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 10.dp, bottom = 5.dp))
                Text(text = "Low   $lowestTemp°",color = Color.White,style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 10.dp))

            }
            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ){
                Spacer(modifier = Modifier.height(20.dp))
                DisplayIcon(icon = favorite.day.iconType, size = 80.dp)//Real
            }
        }
    }
}