package com.example.ut4android.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ut4android.appContext
import com.example.ut4android.data.local.entity.ArticleEntity

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/8
 */
@Database(entities = [ArticleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        private val instance by lazy {
            Room.databaseBuilder(appContext(), AppDatabase::class.java, "my_db").build()
        }

        fun get() = instance
    }
}