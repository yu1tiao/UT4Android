package com.example.ut4android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ut4android.core.BaseViewModel
import com.example.ut4android.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: UserRepository) : BaseViewModel() {

    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean> = _logout

    fun logout() = viewModelScope.launch {
        repository.logout()
            .onSuccess {
                _logout.value = true
            }.onFailure {
                toast(it)
            }
    }
}