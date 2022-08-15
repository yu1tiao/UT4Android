package com.example.ut4android.data.remote.response

import com.example.ut4android.data.local.entity.ArticleEntity
import java.io.Serializable

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/2
 */
data class Article(
    var id: Long,
    var originId: Int,
    var title: String,
    var chapterId: Int,
    var chapterName: String?,
    var envelopePic: Any,
    var link: String,
    var author: String,
    var origin: Any,
    var publishTime: Long,
    var zan: Any,
    var desc: Any,
    var visible: Int,
    var niceDate: String,
    var courseId: Int,
    var collect: Boolean
) : Serializable {

    fun toEntity() = ArticleEntity(
        gid = id,
        originId = originId,
        title = title,
        publishTime = publishTime,
        author = author,
        niceDate = niceDate,
        link = link
    )
}