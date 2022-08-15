package com.example.ut4android.repository

import com.example.ut4android.data.remote.api.UserApi
import com.example.ut4android.data.remote.response.User
import com.example.ut4android.util.fetchResult
import javax.inject.Inject

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */
interface IUserRepository {
    suspend fun login(account: String, password: String): Result<User?>
    suspend fun logout(): Result<Any?>
}

class UserRepository @Inject constructor(private val api: UserApi) : IUserRepository {

    override suspend fun login(account: String, password: String): Result<User?> = fetchResult {
        api.login(account, password)
    }

    override suspend fun logout(): Result<Any?> = fetchResult {
        api.logout()
    }
}