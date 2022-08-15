package com.example.ut4android.core

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.ut4android.util.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/7
 */
abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    protected fun subscribeBasic(vm: BaseViewModel) {
        lifecycleScope.launch {
            vm.loading.collectLatest {
                onLoadingStateChange(it)
            }
        }
        lifecycleScope.launch {
            vm.toast.collectLatest {
                toast(it)
            }
        }
    }

    protected open fun onLoadingStateChange(show: Boolean) {

    }
}