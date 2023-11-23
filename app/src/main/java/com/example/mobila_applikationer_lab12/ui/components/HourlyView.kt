package com.example.mobila_applikationer_lab12.ui.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HourlyView(
    //todo add vm
){
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
                .size(width = 340.dp, height = 130.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Hour()
                Hour()
                Hour()
                Hour()
                Hour()
            }
        }
    }
}

@Composable
fun Hour(
){
    Column(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 12.dp)
            .fillMaxHeight()
        ,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "15:00",color = Color.White)
        Text(text = "7°",color = Color.White)
        Text(text = "45%",color = Color.White)
    }
}