package com.example.mobila_applikationer_lab12.ui.screens
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobila_applikationer_lab12.R
import com.example.mobila_applikationer_lab12.ui.components.HourlyView
import com.example.mobila_applikationer_lab12.ui.components.Overview
import com.example.mobila_applikationer_lab12.ui.components.SearchBar
import com.example.mobila_applikationer_lab12.ui.components.SevenDayView
import com.example.mobila_applikationer_lab12.ui.theme.MobilaApplikationerLab12Theme
import com.example.mobila_applikationer_lab12.ui.theme.Styles.blueBg
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM

@Composable
fun Search(
    vm: WeatherVM,
    navController: NavController
){
    Column( // Container
        modifier = Modifier
            .fillMaxSize()
            .background(blueBg),
        horizontalAlignment = Alignment.CenterHorizontally,

        ){
            SearchBar(vm = vm,navController=navController)
    }
}

