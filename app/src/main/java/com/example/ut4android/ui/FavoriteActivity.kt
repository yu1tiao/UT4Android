package com.example.ut4android.ui

import android.os.Bundle
import androidx.fragment.app.commit
import com.example.ut4android.R
import com.example.ut4android.core.BaseActivity
import com.example.ut4android.databinding.ActivityFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : BaseActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.toolbar)

        supportFragmentManager.commit {
            add(R.id.fragment_container, FavoriteListFragment())
        }
    }
}