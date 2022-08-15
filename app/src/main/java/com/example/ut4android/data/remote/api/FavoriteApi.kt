package com.example.ut4android.data.remote.api

import com.example.ut4android.data.remote.response.PageData
import com.example.ut4android.data.remote.NetResponse
import retrofit2.http.*

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */
interface FavoriteApi {

    @GET("lg/collect/list/{page}/json")
    suspend fun getFavoriteArticleList(
        @Path("page") page: Int
    ): NetResponse<PageData>

    @FormUrlEncoded
    @POST("lg/collect/add/json")
    suspend fun addFavoriteArticle(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("link") link: String,
    ): NetResponse<PageData>

    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    suspend fun removeFavoriteArticle(
        @Path("id") id: Long,
        @Field("originId") originId: Int
    ): NetResponse<PageData>

}