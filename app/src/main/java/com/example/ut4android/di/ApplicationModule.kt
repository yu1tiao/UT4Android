package com.example.ut4android.di

import android.app.Application
import com.example.ut4android.App
import com.example.ut4android.Constants
import com.example.ut4android.data.local.AppDatabase
import com.example.ut4android.data.local.ArticleDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieHandler
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/7
 */
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideApplication(application: Application): App {
        return application as App
    }

    @Provides
    @Named("base_url")
    fun provideBaseUrl(): String {
        return "https://www.wanandroid.com"
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    @DefaultOkHttpClient
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Constants.DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(Constants.DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS)
            .cookieJar(JavaNetCookieJar(CookieHandler.getDefault()))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    @OkHttpClientForDownload
    fun provideOkHttpClientForDownload(@DefaultOkHttpClient client: OkHttpClient): OkHttpClient {
        return client.newBuilder()
            .connectTimeout(Constants.DEFAULT_TIME_OUT_4_DOWNLOAD, TimeUnit.MILLISECONDS)
            .writeTimeout(Constants.DEFAULT_TIME_OUT_4_DOWNLOAD, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.DEFAULT_TIME_OUT_4_DOWNLOAD, TimeUnit.MILLISECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("base_url") baseUrl: String,
        @DefaultOkHttpClient client: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideDatabase(): AppDatabase {
        return AppDatabase.get()
    }

    @Provides
    @Singleton
    fun provideArticle(db: AppDatabase): ArticleDao {
        return db.articleDao()
    }

}