package com.example.ut4android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.ut4android.mock.FakeUserRepository
import com.example.ut4android.support.MainCoroutineRule
import com.example.ut4android.support.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/12
 */

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        viewModel = LoginViewModel(FakeUserRepository())
    }

    @Test
    fun `username is null`() = runTest {
        viewModel.toast.test {
            viewModel.login(null, "11111111")
            assertThat(awaitItem()).isEqualTo("user name is empty")
        }
    }

    @Test
    fun `password is null`() = runTest {
        viewModel.toast.test {
            viewModel.login("leo", null)
            assertThat(awaitItem()).isEqualTo("password is empty")
        }
    }

    @Test
    fun `both username and password are correct`() {
        viewModel.login("leo", "11111111")
        val user = viewModel.login.getOrAwaitValue()
        assertThat(user?.username).isEqualTo("leo")
    }
}