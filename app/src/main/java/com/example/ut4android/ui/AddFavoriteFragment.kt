package com.example.ut4android.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.ut4android.R
import com.example.ut4android.core.BaseFragment
import com.example.ut4android.databinding.FragmentAddFavoriteBinding
import com.example.ut4android.util.toast
import com.example.ut4android.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddFavoriteFragment : BaseFragment(R.layout.fragment_add_favorite) {

    private lateinit var binding: FragmentAddFavoriteBinding
    private val viewModel by activityViewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeBasic(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddFavoriteBinding.bind(view)

        lifecycleScope.launch {
            viewModel.addSuccess.collectLatest {
                onAddSuccess()
            }
        }
        binding.btnAdd2Favorite.setOnClickListener {
            viewModel.addFavoriteArticle(
                binding.etTitle.text.toString().trim(),
                binding.etAuthor.text.toString().trim(),
                binding.etLink.text.toString().trim(),
            )
        }
    }

    override fun onLoadingStateChange(show: Boolean) {
//        binding.progress.isVisible = show
    }

    private fun onAddSuccess() {
        binding.etTitle.setText("")
        binding.etAuthor.setText("")
        binding.etLink.setText("")
        toast("添加成功")
    }
}