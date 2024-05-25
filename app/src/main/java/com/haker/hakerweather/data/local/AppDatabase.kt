package com.haker.hakerweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.haker.hakerweather.data.model.User
import com.haker.hakerweather.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    class Callback @Inject constructor(
        private val database: Provider<AppDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}