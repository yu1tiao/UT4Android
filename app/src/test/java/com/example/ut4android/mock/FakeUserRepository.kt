package com.example.ut4android.mock

import com.example.ut4android.core.ApiException
import com.example.ut4android.data.remote.response.User
import com.example.ut4android.repository.IUserRepository

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/12
 */
class FakeUserRepository : IUserRepository {

    override suspend fun login(account: String, password: String): Result<User?> {
        if (account.isEmpty()) return Result.failure(ApiException("account is empty"))
        if (password.isEmpty()) return Result.failure(ApiException("password is empty"))

        return Result.success(
            User(
                "leo", "11111111", "leo", "leo",
                "leo@spond.com", null, null, 0L, 0
            )
        )
    }

    override suspend fun logout(): Result<Any?> {
        return Result.success("")
    }
}