package com.haker.hakerweather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    var email: String?,
    var password: String?,
    var registerWith: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}
