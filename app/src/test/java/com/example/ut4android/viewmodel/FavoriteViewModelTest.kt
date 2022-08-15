package com.example.ut4android.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.ut4android.mock.FakeFavoriteRepository
import com.example.ut4android.support.MainCoroutineRule
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
class FavoriteViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: FavoriteViewModel

    @Before
    fun setUp() {
        viewModel = FavoriteViewModel(FakeFavoriteRepository())
    }

    @Test
    fun loadFavoriteArticleList() = runTest {
        // test kotlin flow
//        var message = ""
//        val job = launch(UnconfinedTestDispatcher()) {
//            message = viewModel.toast.first()
//        }
//        viewModel.loadFavoriteArticleList(-1)
//        assertThat(message).isEqualTo("invalidate page number")
//        job.cancel()

        // or simply use Turbine
        viewModel.toast.test {
            viewModel.loadFavoriteArticleList(-1)
            assertThat(awaitItem()).isEqualTo("invalidate page number")
        }
    }

    @Test
    fun addFavoriteArticle() = runTest {
        viewModel.toast.test {
            viewModel.addFavoriteArticle("", "xx", "xx")
            assertThat(awaitItem()).isEqualTo("title is empty")

            viewModel.addFavoriteArticle("xx", "", "xx")
            assertThat(awaitItem()).isEqualTo("author is empty")

            viewModel.addFavoriteArticle("xx", "xx", "")
            assertThat(awaitItem()).isEqualTo("url is empty")
        }

        viewModel.addSuccess.test {
            viewModel.addFavoriteArticle("xx", "xx", "xx")
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun removeFavoriteArticle() = runTest {
        viewModel.toast.test {
            viewModel.removeFavoriteArticle(-1, 0)
            assertThat(awaitItem()).isEqualTo("remove from favorite fail")
        }
    }
}