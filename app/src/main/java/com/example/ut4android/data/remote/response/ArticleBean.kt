package com.example.ut4android.data.remote.response

import com.example.ut4android.Article
import java.io.Serializable

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/2
 */
data class ArticleBean(
    var id: Long,
    var originId: Long,
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

    fun toEntity() = Article(
        gid = id,
        originId = originId,
        title = title,
        publishTime = publishTime,
        author = author,
        niceDate = niceDate,
        link = link,
        _id = 0
    )
}