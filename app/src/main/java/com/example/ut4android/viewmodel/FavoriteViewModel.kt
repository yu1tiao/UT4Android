package com.example.ut4android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ut4android.Article
import com.example.ut4android.core.BaseViewModel
import com.example.ut4android.repository.IFavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */
@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: IFavoriteRepository
) : BaseViewModel() {

    val favorites: LiveData<List<Article>> = repository.getFavoriteArticleList(0)

    fun loadFavoriteArticleList(page: Int) = viewModelScope.launch {
        if (page < 0) {
            toast("invalidate page number")
            return@launch
        }
        showLoading()
        repository.loadFavoriteArticleList(page)
            .onFailure {
                toast(it)
            }
        hideLoading()
    }

    private val _addSuccess = MutableSharedFlow<Boolean>()
    val addSuccess: SharedFlow<Boolean> = _addSuccess

    fun addFavoriteArticle(title: String, author: String, link: String) = viewModelScope.launch {
        if (title.isEmpty()) {
            toast("title is empty")
            return@launch
        }
        if (author.isEmpty()) {
            toast("author is empty")
            return@launch
        }
        if (link.isEmpty()) {
            toast("url is empty")
            return@launch
        }
        showLoading()
        repository.addFavoriteArticle(title, author, link)
            .onSuccess {
                _addSuccess.emit(true)
            }
            .onFailure {
                toast(it)
            }
        hideLoading()
    }

    fun removeFavoriteArticle(id: Long, originId: Long) = viewModelScope.launch {
        showLoading()
        repository.removeFavoriteArticle(id, originId)
            .onFailure {
                toast(it)
            }
        hideLoading()
    }

    fun update(entity: Article) = viewModelScope.launch {
        repository.updateArticle(entity)
    }
}