package com.example.ut4android.data.remote

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/7
 */
data class NetResponse<T>(
    val errorCode: Int,
    val data: T?,
    val errorMsg: String?,
) {
    fun isSuccess() = errorCode == 0
}
