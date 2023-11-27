package com.example.mobila_applikationer_lab12.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobila_applikationer_lab12.ui.theme.Styles
import com.example.mobila_applikationer_lab12.ui.theme.Styles.lightBlueCard
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM

@Composable
fun Favorites(
    vm :WeatherVM
){
    Column( // Container
        modifier = Modifier
            .fillMaxSize()
            .background(Styles.blueBg),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){
        favorite()
        favorite()
        favorite()
        favorite()
        favorite()
    }
}

@Composable
fun favorite(){

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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

        }
    }
}