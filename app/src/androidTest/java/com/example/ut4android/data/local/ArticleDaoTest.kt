package com.example.ut4android.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.filters.MediumTest
import com.example.ut4android.App.Companion.context
import com.example.ut4android.data.local.entity.ArticleEntity
import com.example.ut4android.support.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsEmptyCollection
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Copyright (c) Spond All rights reserved.
 *
 * @author leo
 * @date 2022/8/13
 */
@MediumTest
@ExperimentalCoroutinesApi
class ArticleDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: ArticleDao
    private lateinit var article: ArticleEntity

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.articleDao()

        val time = System.currentTimeMillis()
        article = ArticleEntity(1, 2, "a1", time, "baidu", "leo", "2022-08-08")
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getAllArticles() {
        val articles = dao.getAllArticles()
        val data = articles.getOrAwaitValue()
        assertThat(data, IsEmptyCollection())
    }

    @Test
    fun insert() = runTest {
        val ids = dao.insert(article)
        assert(ids.isNotEmpty())

        val query = dao.queryById(1)
        assertEquals(query, article)
    }

    @Test
    fun update() = runTest {
        val ids = dao.insert(article)
        assert(ids.isNotEmpty())

        val query = dao.queryById(1)
        assert(query != null)

        query?.let {
            it.title = "updated"
            val update = dao.update(it)
            assert(update > 0)
            val updated = dao.queryById(1)
            assertEquals(updated?.title, "updated")
        }
    }

    @Test
    fun deleteById() = runTest {
        val ids = dao.insert(article)
        assert(ids.isNotEmpty())

        val count = dao.deleteById(article.gid)
        assertEquals(count, 1)

        val data = dao.queryById(article.gid)
        assertEquals(data, null)
    }

    @Test
    fun queryById() = runTest {
        val ids = dao.insert(article)
        assert(ids.isNotEmpty())

        val data = dao.queryById(article.gid)
        assertEquals(data, article)
    }
}