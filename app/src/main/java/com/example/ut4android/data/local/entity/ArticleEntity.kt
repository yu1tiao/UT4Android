package com.example.ut4android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/8
 */
@Entity
data class ArticleEntity(
    @SerializedName("id")
    var gid: Long,
    var originId: Int,
    var title: String,
    var publishTime: Long,
    var link: String,
    var author: String,
    var niceDate: String
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var _id: Long? = null
}