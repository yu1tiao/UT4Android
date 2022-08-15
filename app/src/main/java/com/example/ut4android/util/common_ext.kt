package com.example.ut4android.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.ut4android.data.remote.NetResponse
import com.example.ut4android.appContext
import com.example.ut4android.core.ApiException

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */

const val TAG = "log_tag"

fun logi(message: String?) = message?.let {
    Log.i(TAG, it)
}

fun toast(throwable: Throwable?) {
    if (throwable == null) return
    Toast.makeText(appContext(), throwable.message, Toast.LENGTH_SHORT).show()
}

fun toast(message: String?) {
    if (message.isNullOrEmpty()) return
    Toast.makeText(appContext(), message, Toast.LENGTH_SHORT).show()
}

fun sharedPreferences() = appContext().getSharedPreferences("pref", Context.MODE_PRIVATE)!!

inline fun <T> fetchResult(block: () -> NetResponse<T>): Result<T?> {
    return runCatching {
        val resp = block()
        if (resp.isSuccess()) {
            resp.data
        } else {
            throw ApiException(resp.errorMsg)
        }
    }
}