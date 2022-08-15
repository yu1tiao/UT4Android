package com.example.ut4android.core.permission

import androidx.fragment.app.FragmentActivity

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/26
 */
object PermissionManager {

    @Volatile
    private var currentRequest: PermissionRequest? = null

    @JvmStatic
    fun with(
        activity: FragmentActivity, block: PermissionRequest.Builder.() -> Unit
    ): PermissionRequest {
        return PermissionRequest.Builder(activity)
            .apply(block)
            .build()
            .also { currentRequest = it }
    }

    internal fun cancelRequest() {
        currentRequest = null
    }

    internal fun handleRequestResult(result: Map<String, Boolean>) {
        val request = currentRequest ?: return
        if (result.isEmpty()) return

        val deniedMap = result.filterValues { !it }
        val allGranted = deniedMap.isEmpty()

        if (allGranted) {
            request.callGranted()
        } else {
            request.callDenied(deniedMap.keys)
        }
    }
}
