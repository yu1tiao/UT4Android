package com.example.ut4android.data.remote.api

import com.example.ut4android.data.remote.response.User
import com.example.ut4android.data.remote.NetResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */
interface UserApi {

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): NetResponse<User>

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): NetResponse<User>

    @GET("user/logout/json")
    suspend fun logout(): NetResponse<Any>
}