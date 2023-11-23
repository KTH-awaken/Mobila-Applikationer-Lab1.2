package com.example.mobila_applikationer_lab12.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mobila_applikationer_lab12.ui.components.HourlyView
import com.example.mobila_applikationer_lab12.ui.components.Overview
import com.example.mobila_applikationer_lab12.ui.components.SevenDayView
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM

@Composable
fun Home(
    vm :WeatherVM
    //todo add nav controller
){
    Column( // Container
        modifier = Modifier
            .fillMaxSize()
            .background(Color(33, 36, 74)),
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
        vm.fetchWeatherData()
        Overview(vm = vm)
        HourlyView()
        SevenDayView()
    }
}


