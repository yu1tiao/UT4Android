package com.example.ut4android.core

import kotlinx.coroutines.flow.flow

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/1
 */
enum class LoadPolicy {
    CACHE_FIRST_THEN_NETWORK, CACHE_ONLY_IF_EFFECTIVE, NETWORK_ONLY,
}

abstract class BaseLoader<T> {

    fun load(policy: LoadPolicy = LoadPolicy.CACHE_FIRST_THEN_NETWORK) = flow {
        // load from cache
        val localData = try {
            fetchDataFromLocal()
        } catch (e: Exception) {
            Result.failure(e)
        }
        if (policy != LoadPolicy.NETWORK_ONLY && localData.isSuccess
            && !isLocalDataOutdated(localData.getOrThrow())
        ) {
            emit(localData)
            if (policy == LoadPolicy.CACHE_ONLY_IF_EFFECTIVE) {
                return@flow
            }
        }

        // load from remote
        val remoteData = try {
            fetchDataFromRemote()
        } catch (e: Exception) {
            Result.failure(e)
        }
        if (remoteData.isSuccess) {
            if (localData.isFailure ||
                !isDataEqualsCache(localData.getOrThrow(), remoteData.getOrThrow())
            ) {
                saveDataToCache(System.currentTimeMillis(), remoteData.getOrThrow())
                emit(remoteData)
            }
            return@flow
        }

        // both local request and remote request are fail.
        if (localData.isFailure && remoteData.isFailure) {
            emit(remoteData)
        }
    }

    open fun isDataEqualsCache(cache: T, remote: T): Boolean {
        return cache == remote
    }

    abstract suspend fun fetchDataFromLocal(): Result<T>
    abstract suspend fun fetchDataFromRemote(): Result<T>
    abstract fun isLocalDataOutdated(data: T): Boolean
    abstract fun saveDataToCache(fetchTime: Long, data: T)
}