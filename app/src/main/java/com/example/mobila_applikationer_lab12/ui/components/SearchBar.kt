package com.example.mobila_applikationer_lab12.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobila_applikationer_lab12.ui.theme.Styles.blueAction
import com.example.mobila_applikationer_lab12.ui.viewmodels.WeatherVM
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    vm: WeatherVM,
    navController: NavController
) {
    var text by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 10.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            placeholder = { Text("Search...", color = Color.White) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            leadingIcon = {
                Icon(imageVector =  Icons.Outlined.Search, contentDescription ="Search icon", tint = Color.White)
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            text = ""
                        }
                    ) {
                        Icon(imageVector = Icons.Outlined.Clear, contentDescription = "Clear icon",tint = Color.White)
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {

                    vm.setCityToShow(text)
                    runBlocking {
                        vm.fetchWeatherData()
                    }
                    isSearchActive = false

                    keyboardController?.hide()
                    navController.navigate("home")
                }
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = blueAction,
                unfocusedBorderColor = blueAction.copy(alpha = 0.5f),
                cursorColor = blueAction,
                textColor = Color.White,
            )
        )

    }
}