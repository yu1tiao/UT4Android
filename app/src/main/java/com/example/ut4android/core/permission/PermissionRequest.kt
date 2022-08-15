package com.example.ut4android.core.permission

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/26
 */
class PermissionRequest(private val activity: FragmentActivity) {

    private var explainCallback: ((PermissionRequest) -> Unit)? = null
    private var rationaleCallback: ((PermissionRequest) -> Unit)? = null
    private var neverAskCallback: (() -> Unit)? = null
    private var grantedCallback: (() -> Unit)? = null
    private var deniedCallback: ((Set<String>) -> Unit)? = null
    private var permissions: Array<String>? = null

    fun cancelRequest() {
        PermissionManager.cancelRequest()
    }

    fun continueRequest() {
        permissions?.let { requestEach(it) }
    }

    internal fun callGranted() {
        grantedCallback?.invoke()
        permissions?.forEach {
            PermissionStorage.setNeverAsk(it, false)
        }
    }

    internal fun callDenied(set: Set<String>) {
        if (neverAskCallback != null) {
            var needCallNeverAskCallback = false
            set.forEach {
                if (PermissionStorage.isNeverAsk(it)) {
                    needCallNeverAskCallback = true
                    return@forEach
                }
            }
            if (needCallNeverAskCallback) {
                neverAskCallback?.invoke()
                return
            }
        }
        deniedCallback?.invoke(set)
    }

    private fun requestEach(permissions: Array<String>) {
        with(activity.supportFragmentManager) {
            val f = RequestPermissionFragment()
            beginTransaction().add(f, "requestPermissionFromActivity").commit()
            executePendingTransactions()
            f.requestPermissions(permissions)
        }
    }

    private fun havePermissions(permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        permissions.forEach {
            if (ActivityCompat.checkSelfPermission(activity, it)
                != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    private fun shouldShowRequestPermissionRationale(permissions: Array<String>): Boolean {
        permissions.forEach {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, it)) {
                return true
            }
        }
        return false
    }

    private fun isNeverAskPermission(permission: String): Boolean {
        val lastRationale = PermissionStorage.lastShouldShowRequestPermissionRationale(permission)
        val currentRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)

        if (currentRationale) {
            PermissionStorage.setLastShouldShowRequestPermissionRationale(permission, true)
        }

        return lastRationale && !currentRationale
    }

    fun start() {
        val ps = requireNotNull(permissions) { "permissions = null" }

        if (havePermissions(ps)) {
            callGranted()
            return
        }

        var hasNeverAskPermission = false
        ps.forEach {
            if (isNeverAskPermission(it)) {
                PermissionStorage.setNeverAsk(it, true)
                hasNeverAskPermission = true
            }
        }

        if (!hasNeverAskPermission) {
            if (rationaleCallback != null && shouldShowRequestPermissionRationale(ps)) {
                rationaleCallback?.invoke(this)
            } else if (explainCallback != null) {
                explainCallback?.invoke(this)
            } else {
                continueRequest()
            }
        } else {
            continueRequest()
        }
    }

    class Builder(private val activity: FragmentActivity) {

        private var permissions: Array<String>? = null
        private var explainCallback: ((PermissionRequest) -> Unit)? = null
        private var rationaleCallback: ((PermissionRequest) -> Unit)? = null
        private var neverAskCallback: (() -> Unit)? = null
        private var grantedCallback: (() -> Unit)? = null
        private var deniedCallback: ((Set<String>) -> Unit)? = null

        fun permissions(vararg permission: String) = apply {
            this.permissions = arrayOf(*permission)
        }

        fun onExplain(callback: (PermissionRequest) -> Unit) = apply {
            this.explainCallback = callback
        }

        fun onNeverAsk(callback: () -> Unit) = apply {
            this.neverAskCallback = callback
        }

        fun onGranted(callback: () -> Unit) = apply {
            this.grantedCallback = callback
        }

        fun onDenied(callback: (Set<String>) -> Unit) = apply {
            this.deniedCallback = callback
        }

        fun onRationale(callback: (PermissionRequest) -> Unit) = apply {
            this.rationaleCallback = callback
        }

        fun build(): PermissionRequest {
            return PermissionRequest(activity).apply {
                this.permissions = this@Builder.permissions
                this.rationaleCallback = this@Builder.rationaleCallback
                this.grantedCallback = this@Builder.grantedCallback
                this.deniedCallback = this@Builder.deniedCallback
                this.explainCallback = this@Builder.explainCallback
                this.neverAskCallback = this@Builder.neverAskCallback
            }
        }
    }
}