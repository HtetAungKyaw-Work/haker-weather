package com.haker.hakerweather.data.model

data class SportsResponse(
    val football: MutableList<Sport>,
    val cricket: MutableList<Sport>,
    val golf: MutableList<Sport>
)
