package com.example.ut4android.core.permission

import android.content.Context
import androidx.core.content.edit
import com.example.ut4android.appContext

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/26
 */
object PermissionStorage {

    private const val NEVER_ASK = "never_ask"
    private const val LAST_RATIONALE = "last_rationale"

    private val sp by lazy {
        appContext().getSharedPreferences("permission_storage", Context.MODE_PRIVATE)
    }

    internal fun setLastShouldShowRequestPermissionRationale(
        permission: String, rationale: Boolean
    ) {
        sp.edit(true) {
            val key = "$LAST_RATIONALE${permission}"
            putBoolean(key, rationale)
        }
    }

    internal fun lastShouldShowRequestPermissionRationale(permission: String): Boolean {
        val key = "$LAST_RATIONALE${permission}"
        return sp.getBoolean(key, false)
    }

    internal fun setNeverAsk(permission: String, neverAsk: Boolean) {
        sp.edit(true) {
            val key = "$NEVER_ASK${permission}"
            putBoolean(key, neverAsk)
        }
    }

    internal fun isNeverAsk(permission: String): Boolean {
        val key = "$NEVER_ASK$permission"
        return sp.getBoolean(key, false)
    }
}