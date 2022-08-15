package com.example.ut4android.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.ut4android.data.local.entity.ArticleEntity
import com.example.ut4android.repository.IFavoriteRepository

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/12
 */
class FakeFavoriteRepository : IFavoriteRepository {

    private val time = System.currentTimeMillis()
    private val mockData = listOf(
        ArticleEntity(1, 1, "title1", time, "http://www.baidu.com", "1", "08-08"),
        ArticleEntity(2, 2, "title2", time, "http://www.baidu.com", "2", "08-08"),
    )

    override fun getFavoriteArticleList(page: Int): LiveData<List<ArticleEntity>> {
        return liveData {
            if (page >= 0) {
                emit(mockData)
            } else {
                emit(emptyList())
            }
        }
    }

    override suspend fun loadFavoriteArticleList(page: Int): Result<List<ArticleEntity>> {
        return if (page >= 0) {
            Result.success(mockData)
        } else {
            Result.failure(RuntimeException("page < 0"))
        }
    }

    override suspend fun addFavoriteArticle(
        title: String, author: String, link: String
    ): Result<Boolean> {
        return if (title.isNotEmpty() && author.isNotEmpty() && link.isNotEmpty()) {
            Result.success(true)
        } else {
            Result.failure(RuntimeException("add  to favorite fail"))
        }
    }

    override suspend fun removeFavoriteArticle(id: Long, originId: Int): Result<Boolean> {
        return if (id >= 0) {
            Result.success(true)
        } else {
            Result.failure(RuntimeException("remove from favorite fail"))
        }
    }

    override suspend fun updateArticle(entity: ArticleEntity): Result<Boolean> {
        return Result.success(true)
    }
}