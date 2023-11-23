package com.example.mobila_applikationer_lab12.model.data

data class Place(
    val place_id: Int,
    val licence: String,
    val powered_by: String,
    val osm_type: String,
    val osm_id: Long,
    val boundingbox: List<Double>,
    val lat: Double,
    val lon: Double,
    val display_name: String,
    val `class`: String,
    val type: String,
    val importance: Double
)

data class BoundingBox(
    val minLat: String,
    val maxLat: String,
    val minLon: String,
    val maxLon: String
)