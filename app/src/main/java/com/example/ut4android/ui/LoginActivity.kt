package com.example.ut4android.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.ut4android.core.BaseActivity
import com.example.ut4android.databinding.ActivityLoginBinding
import com.example.ut4android.util.toast
import com.example.ut4android.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding
    private val jump2Main by lazy { intent.getBooleanExtra(EXTRA_JUMP_2_MAIN, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscribeBasic(viewModel)
        binding.btnLogin.setOnClickListener(::btnLogin)

        binding.etUserName.addTextChangedListener {
            tryResetLoginButtonStatus()
        }
        binding.etPassword.addTextChangedListener {
            tryResetLoginButtonStatus()
        }

        viewModel.login.observe(this) {
            if (jump2Main) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
            finish()
        }
    }

    private fun tryResetLoginButtonStatus() {
        binding.btnLogin.isEnabled =
            !binding.etUserName.text.isNullOrEmpty() && !binding.etPassword.text.isNullOrEmpty()
    }

    override fun onLoadingStateChange(show: Boolean) {
        binding.progress.isVisible = show
    }

    private fun btnLogin(view: View) {
        val account = binding.etUserName.text.toString()
        val password = binding.etPassword.text.toString()
        viewModel.login(account, password)
    }

    companion object {
        private const val EXTRA_JUMP_2_MAIN = "jump_2_main"

        fun createIntent(context: Context, jump2Main: Boolean = false) =
            Intent(context, LoginActivity::class.java).apply {
                putExtra(EXTRA_JUMP_2_MAIN, jump2Main)
            }
    }
}