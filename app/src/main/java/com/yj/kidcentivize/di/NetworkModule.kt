package com.yj.kidcentivize.di

import com.yj.kidcentivize.api.PlzmaService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providePlzmaService(): PlzmaService {
        return PlzmaService.create()
    }
}
