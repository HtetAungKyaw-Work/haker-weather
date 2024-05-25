package com.haker.hakerweather.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haker.hakerweather.data.model.User

@Dao
interface UserDao {

    @Query("select * from users where email = :email")
    fun getUserByEmail(email: String): LiveData<List<User>>

    @Query("select * from users where email = :email and password = :password")
    fun login(email: String, password: String): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User) : Long
}