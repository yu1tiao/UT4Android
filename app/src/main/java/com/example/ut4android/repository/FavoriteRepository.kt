package com.example.ut4android.repository

import androidx.lifecycle.LiveData
import com.example.ut4android.Article
import com.example.ut4android.core.ApiException
import com.example.ut4android.data.local.ArticleDao
import com.example.ut4android.data.remote.api.FavoriteApi
import com.example.ut4android.util.fetchResult
import javax.inject.Inject

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */
interface IFavoriteRepository {
    fun getFavoriteArticleList(page: Int): LiveData<List<Article>>
    suspend fun loadFavoriteArticleList(page: Int): Result<List<Article>>
    suspend fun addFavoriteArticle(title: String, author: String, link: String): Result<Boolean>
    suspend fun removeFavoriteArticle(id: Long, originId: Long): Result<Boolean>
    suspend fun updateArticle(entity: Article): Result<Boolean>
}

class FavoriteRepository @Inject constructor(
    private val api: FavoriteApi,
    private val dao: ArticleDao,
) : IFavoriteRepository {

    override fun getFavoriteArticleList(page: Int): LiveData<List<Article>> {
        return dao.getAllArticles()
    }

    override suspend fun loadFavoriteArticleList(page: Int): Result<List<Article>> {
        val remoteResult = fetchResult {
            api.getFavoriteArticleList(page)
        }
        return if (remoteResult.isSuccess) {
            val pageData = remoteResult.getOrNull()
            val dataList = pageData?.datas.orEmpty()
            val entities = dataList.map { it.toEntity() }

            entities.forEach {
                val entity = dao.queryById(it.gid)
                if (entity == null) {
                    dao.insert(it)
                } else {
                    dao.update(it)
                }
            }
            Result.success(entities)
        } else {
            Result.failure(
                remoteResult.exceptionOrNull() ?: ApiException("add to favorite fail")
            )
        }
    }

    override suspend fun addFavoriteArticle(
        title: String, author: String, link: String
    ): Result<Boolean> {
        val remoteResult = fetchResult {
            api.addFavoriteArticle(title, author, link)
        }
        return if (remoteResult.isSuccess) {
            remoteResult.getOrNull()?.datas?.find {
                it.title == title && it.author == author && it.link == link
            }?.let {
                dao.insert(it.toEntity())
            }
            Result.success(true)
        } else {
            Result.failure(
                remoteResult.exceptionOrNull() ?: ApiException("add to favorite fail")
            )
        }
    }

    override suspend fun removeFavoriteArticle(id: Long, originId: Long): Result<Boolean> {
        val remoteResult = fetchResult {
            api.removeFavoriteArticle(id, originId)
        }
        return if (remoteResult.isSuccess) {
            dao.deleteById(id)
            Result.success(true)
        } else {
            Result.failure(
                remoteResult.exceptionOrNull() ?: ApiException("remove favorite fail")
            )
        }
    }

    override suspend fun updateArticle(entity: Article): Result<Boolean> {
        return try {
            dao.update(entity)
            Result.success(true)
        } catch (e: Exception) {
            Result.success(false)
        }
    }
}