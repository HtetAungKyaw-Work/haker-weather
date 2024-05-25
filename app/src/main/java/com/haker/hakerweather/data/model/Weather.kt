package com.haker.hakerweather.data.model

data class Weather(
    var id : Int? = null,
    val name: String?,
    val region: String?,
    val country: String?,
    val lat: Double?,
    val lon: Double?,
    val url: String?
)