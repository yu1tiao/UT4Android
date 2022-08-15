package com.example.ut4android

import android.app.Application
import android.content.Context
import com.example.ut4android.core.cookie.PersistentCookieStore
import dagger.hilt.android.HiltAndroidApp
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.properties.Delegates

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this

        CookieHandler.setDefault(
            CookieManager(PersistentCookieStore.getInstance(), CookiePolicy.ACCEPT_ALL)
        )
    }

    companion object {
        @JvmStatic
        var context: Context by Delegates.notNull()
    }
}

fun appContext() = App.context