package com.example.ut4android.di

import com.example.ut4android.data.remote.api.FavoriteApi
import com.example.ut4android.data.remote.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/7
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFavoriteApi(retrofit: Retrofit): FavoriteApi {
        return retrofit.create(FavoriteApi::class.java)
    }
}