package com.example.ut4android.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ut4android.data.local.entity.ArticleEntity

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/8
 */
@Dao
interface ArticleDao {

    @Query("select * from ArticleEntity")
    fun getAllArticles(): LiveData<List<ArticleEntity>>

    @Query("select * from ArticleEntity where gid = :gid")
    suspend fun queryById(gid: Long): ArticleEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: ArticleEntity): List<Long>

    @Query("delete from ArticleEntity where gid = :gid")
    suspend fun deleteById(gid: Long): Int

    @Update
    suspend fun update(entity: ArticleEntity): Int
}