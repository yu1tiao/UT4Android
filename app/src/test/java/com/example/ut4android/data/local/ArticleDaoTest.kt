package com.example.ut4android.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ut4android.Article
import com.example.ut4android.Database
import com.example.ut4android.support.MainCoroutineRule
import com.example.ut4android.support.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/16
 */
@ExperimentalCoroutinesApi
class ArticleDaoTest {

    private lateinit var driver: JdbcSqliteDriver
    private lateinit var dao: ArticleDao
    private lateinit var article: Article

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        Database.Schema.create(driver)
        dao = ArticleDaoImpl(Database(driver))

        val time = System.currentTimeMillis()
        article = Article(0, 1, 2, "a1", "baidu", "leo", "2022-08-08", time)
    }

    @After
    fun tearDown() {
        driver.close()
    }

    @Test
    fun getAllArticles() = runTest {
        var articles = dao.getAllArticles()
        var data = articles.getOrAwaitValue()
        assertThat(data).isEmpty()

        dao.insert(article)
        articles = dao.getAllArticles()
        data = articles.getOrAwaitValue()
        assertThat(data).hasSize(1)
        assertThat(data[0].niceDate).isEqualTo(article.niceDate)
    }

    @Test
    fun queryById() = runTest {
        dao.insert(article)
        val queried = dao.queryById(article.gid)
        assertThat(queried?.publishTime).isEqualTo(article.publishTime)
    }

    @Test
    fun deleteById() = runTest {
        dao.insert(article)
        var list = dao.getAllArticles().getOrAwaitValue()
        assertThat(list).hasSize(1)
        dao.deleteById(article.gid)
        list = dao.getAllArticles().getOrAwaitValue()
        assertThat(list).isEmpty()
    }

    @Test
    fun update() = runTest {
        dao.insert(article)
        dao.update(article.copy(title = "qwe", author = "asd"))
        val queried = dao.queryById(article.gid)
        assertThat(queried?.title).isEqualTo("qwe")
        assertThat(queried?.author).isEqualTo("asd")
    }
}