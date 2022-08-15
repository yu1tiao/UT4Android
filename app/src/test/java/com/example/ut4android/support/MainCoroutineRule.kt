package com.example.ut4android.support

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/12
 */
@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
) : TestWatcher(), CoroutineScope by TestScope(dispatcher) {

    override fun starting(description: Description?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}