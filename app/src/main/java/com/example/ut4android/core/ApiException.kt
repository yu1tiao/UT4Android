package com.example.ut4android.core

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */
class ApiException @JvmOverloads constructor(message: String?, throwable: Throwable? = null) :
    RuntimeException(message, throwable)