package com.example.ut4android.data.remote.response

import java.io.Serializable

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/7
 */
data class PageData(
    var offset: Int,
    var size: Int,
    var total: Int,
    var pageCount: Int,
    var curPage: Int,
    var over: Boolean,
    var datas: List<Article>?
) : Serializable