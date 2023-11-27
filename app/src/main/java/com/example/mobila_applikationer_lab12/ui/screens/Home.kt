package com.example.mobila_applikationer_lab12.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.mobila_applikationer_lab12.ui.components.HourlyView
import com.example.mobila_applikationer_lab12.ui.components.Overview
import com.example.mobila_applikationer_lab12.ui.components.SevenDayView
import com.example.mobila_applikationer_lab12.ui.theme.Styles.blueBg
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM

//Scafold verson
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    vm :WeatherVM,
    navController: NavController
    //todo add nav controller
){

    Column( // Container
        modifier = Modifier
            .fillMaxSize()
            .background(blueBg),
        horizontalAlignment = Alignment.CenterHorizontally,

        ){
        Overview(vm = vm)
        HourlyView(vm = vm)
        SevenDayView(vm = vm)
    }
}


