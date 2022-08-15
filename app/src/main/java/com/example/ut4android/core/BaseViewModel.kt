package com.example.ut4android.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/2
 */
abstract class BaseViewModel : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _toast: Channel<String> = Channel()
    val toast = _toast.receiveAsFlow()

    protected fun toast(message: String?) {
        if (message.isNullOrEmpty()) return
        viewModelScope.launch {
            _toast.send(message)
        }
    }

    protected fun toast(error: Throwable) {
        toast(error.message)
    }

    protected fun showLoading() {
        viewModelScope.launch {
            _loading.emit(true)
        }
    }

    protected fun hideLoading() {
        viewModelScope.launch {
            _loading.emit(false)
        }
    }
}