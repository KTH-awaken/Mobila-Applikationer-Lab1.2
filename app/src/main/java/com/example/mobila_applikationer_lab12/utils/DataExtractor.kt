package com.example.mobila_applikationer_lab12.utils

import com.example.mobila_applikationer_lab12.model.data.Forecast

fun Forecast.getCurrentAreaTemperature(): Double? {
    val temperatureParameter = "t"

    // Iterate through time series
    for (timeSeries in timeSeries) {
        // Iterate through parameters in each time series
        for (parameter in timeSeries.parameters) {
            // Check if the parameter is temperature
            if (parameter.name == temperatureParameter) {
                // Assuming there is only one value for temperature
                if (parameter.values.isNotEmpty()) {
                    return parameter.values[0]
                }
            }
        }
    }

    // Return null if temperature is not found
    return null
}