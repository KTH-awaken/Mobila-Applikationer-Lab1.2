package com.example.mobila_applikationer_lab12.model.data

import kotlinx.serialization.Serializable

data class Weather(
    val error: Boolean,
    val tmp: String,
)
data class Forecast(
    val approvedTime: String,
    val referenceTime: String,
    val geometry: Geometry,
    val timeSeries: List<TimeSeries>
)

data class Geometry(
    val type: String,
    val coordinates: List<List<Double>>
)

data class TimeSeries(
    val validTime: String,
    val parameters: List<Parameter>
)

data class Parameter(
    val name: String,
    val levelType: String,
    val level: Int,
    val unit: String,
    val values: List<Double>
)
