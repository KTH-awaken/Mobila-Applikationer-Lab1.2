package com.example.mobila_applikationer_lab12.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mobila_applikationer_lab12.ui.theme.Styles.lightBlueText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun InternetStatus(show: Boolean,hasInternet:Boolean,){
    if (show){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            Arrangement.SpaceBetween
//            horizontalArrangement= Arrangement.Start
        ) {
                if (hasInternet){
                    Text(text = "Connected", color =lightBlueText,)
                }else{
                    Text(text ="No internet", color = lightBlueText)
                }
            val currentDateTime = LocalDateTime.now()
            Text(text = DateTimeFormatter.ofPattern("HH:mm").format(currentDateTime), color = lightBlueText, modifier = Modifier.padding(end=5.dp))

        }
    }
}