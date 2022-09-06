package com.example.ut4android.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewbinding.ViewBinding
import com.example.ut4android.R
import com.example.ut4android.appContext
import com.example.ut4android.util.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/7
 */
abstract class BaseActivity : AppCompatActivity() {


    protected fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
        toolbar.navigationIcon =
            AppCompatResources.getDrawable(this, R.mipmap.baseline_arrow_back_black_24)
                ?.apply { setTint(Color.WHITE) }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    protected fun subscribeBasic(vm: BaseViewModel) {
        lifecycleScope.launch {
            vm.loading.collectLatest { onLoadingStateChange(it) }
        }
        lifecycleScope.launch {
            vm.toast.collectLatest { toast(it) }
        }
    }

    protected open fun onLoadingStateChange(show: Boolean) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBroadcast()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcast()
    }

    open var receiver: BroadcastReceiver? = null

    protected open fun registerBroadcast() {
        val intentFilter = IntentFilter()
        getBroadcastAction().forEach {
            intentFilter.addAction(it)
        }
        if (receiver == null) {
            receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val flag = intent.getSerializableExtra("finish_skip")
                    if (flag != null && this@BaseActivity.javaClass == flag) return
                    finish()
                }
            }
        }
        receiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(it, intentFilter)
        }
    }

    protected open fun getBroadcastAction(): Array<String> {
        return arrayOf("FinishBroadcastActivity")
    }

    protected open fun unregisterBroadcast() {
        receiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
            receiver = null
        }
    }

    companion object {

        @JvmStatic
        @JvmOverloads
        fun finishAll(skip: Serializable? = null) {
            val intent = Intent().setAction("FinishBroadcastActivity")
            skip?.let {
                intent.putExtra("finish_skip", skip)
            }
            LocalBroadcastManager.getInstance(appContext()).sendBroadcast(intent)
        }
    }
}