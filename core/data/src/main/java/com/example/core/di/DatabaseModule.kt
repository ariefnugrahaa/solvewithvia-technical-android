package com.example.core.di

import android.content.Context
import androidx.room.Room
import com.example.core.data.local.RequestDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRequestDatabase(
        @ApplicationContext context: Context
    ): RequestDatabase {
        return Room.databaseBuilder(
            context,
            RequestDatabase::class.java,
            "request_db"
        ).build()
    }
}
