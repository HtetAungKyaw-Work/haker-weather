package com.haker.hakerweather.di

import android.app.Application
import androidx.room.Room
import com.haker.hakerweather.data.local.AppDatabase
import com.haker.hakerweather.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: AppDatabase.Callback): AppDatabase{
        return Room.databaseBuilder(application, AppDatabase::class.java, "haker_weather_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideArticleDao(db: AppDatabase): UserDao {
        return db.getUserDao()
    }
}