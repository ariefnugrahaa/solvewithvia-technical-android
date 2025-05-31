package com.example.core.di

import android.content.Context
import com.example.core.data.local.RequestDatabase
import com.example.core.data.repository.RequestRepositoryImpl
import com.example.core.data.repository.SettingsRepositoryImpl
import com.example.core.domain.repository.RequestRepository
import com.example.core.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRequestRepository(
        db: RequestDatabase
    ): RequestRepository {
        return RequestRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext context: Context
    ): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}


