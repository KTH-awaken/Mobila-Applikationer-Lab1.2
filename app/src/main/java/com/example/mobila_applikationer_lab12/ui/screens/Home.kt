package com.example.mobila_applikationer_lab12.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.mobila_applikationer_lab12.ui.components.Overview

@Composable
fun Home(
    //todo add vm
    //todo add nav controller
){
    Column( // Container
        modifier = Modifier
            .background(Color(176, 178, 176)),
        verticalArrangement = Arrangement.Center,
    ){
        Overview()
    }
}


