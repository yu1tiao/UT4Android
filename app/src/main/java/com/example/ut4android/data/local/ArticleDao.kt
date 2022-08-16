package com.example.ut4android.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.ut4android.Article
import com.example.ut4android.Database
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/8
 */
interface ArticleDao {

    fun getAllArticles(): LiveData<List<Article>>

    suspend fun queryById(gid: Long): Article?

    suspend fun insert(vararg entity: Article)

    suspend fun deleteById(gid: Long)

    suspend fun update(entity: Article)
}

class ArticleDaoImpl @Inject constructor(private val db: Database) : ArticleDao {

    override fun getAllArticles(): LiveData<List<Article>> {
        return db._articleQueries.selectAll()
            .asFlow()
            .map { it.executeAsList() }
            .asLiveData()
    }

    override suspend fun queryById(gid: Long): Article? = withContext(Dispatchers.IO) {
        db._articleQueries.selectById(gid).executeAsOneOrNull()
    }

    override suspend fun insert(vararg entity: Article) = withContext(Dispatchers.IO) {
        entity.forEach {
            db._articleQueries.insert(
                it.gid, it.originId, it.title, it.link, it.author, it.niceDate, it.publishTime
            )
        }
    }

    override suspend fun deleteById(gid: Long) = withContext(Dispatchers.IO) {
        db._articleQueries.deleteById(gid)
    }

    override suspend fun update(entity: Article) = withContext(Dispatchers.IO) {
        db._articleQueries.update(
            entity.title,
            entity.link,
            entity.author,
            entity.niceDate,
            entity.publishTime,
            entity.gid
        )
    }
}