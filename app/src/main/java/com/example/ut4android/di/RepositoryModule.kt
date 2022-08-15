package com.example.ut4android.di

import com.example.ut4android.repository.FavoriteRepository
import com.example.ut4android.repository.IFavoriteRepository
import com.example.ut4android.repository.IUserRepository
import com.example.ut4android.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/7
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsUserRepository(repository: UserRepository): IUserRepository

    @Binds
    abstract fun bindsFavoriteRepository(repository: FavoriteRepository): IFavoriteRepository
}