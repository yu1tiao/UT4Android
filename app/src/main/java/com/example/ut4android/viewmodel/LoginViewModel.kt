package com.example.ut4android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ut4android.core.BaseViewModel
import com.example.ut4android.data.remote.response.User
import com.example.ut4android.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/1
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IUserRepository
) : BaseViewModel() {

    private val _login = MutableLiveData<User?>()
    val login: LiveData<User?> = _login

    fun login(account: String?, password: String?) = viewModelScope.launch {
        if (account.isNullOrEmpty()) {
            toast("user name is empty")
            return@launch
        }
        if (password.isNullOrEmpty()) {
            toast("password is empty")
            return@launch
        }

        showLoading()
        repository.login(account, password)
            .onSuccess {
                _login.value = it
            }.onFailure {
                toast(it)
            }
        hideLoading()
    }
}