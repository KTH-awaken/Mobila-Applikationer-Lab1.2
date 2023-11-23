package com.example.mobila_applikationer_lab12.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun Overview(
    //todo add vm
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "7°",
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = "Stockholm",
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}