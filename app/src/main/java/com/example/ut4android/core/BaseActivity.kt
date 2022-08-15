package com.example.ut4android.core

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.ut4android.R
import com.example.ut4android.util.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
}