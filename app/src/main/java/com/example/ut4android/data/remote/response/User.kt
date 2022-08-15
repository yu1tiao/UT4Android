package com.example.ut4android.data.remote.response

import java.io.Serializable

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/7
 */
data class User(
    val username: String?,
    val password: String?,
    val publicName: String?,
    val nickname: String?,
    val email: String?,
    val icon: String?,
    val token: String?,
    val id: Long,
    val type: Int
) : Serializable