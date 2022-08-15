package com.example.ut4android.ui

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ut4android.R
import com.example.ut4android.data.remote.response.Article
import com.example.ut4android.core.BaseFragment
import com.example.ut4android.data.local.entity.ArticleEntity
import com.example.ut4android.databinding.FragmentFavoriteListBinding
import com.example.ut4android.databinding.ItemFavoriteBinding
import com.example.ut4android.util.toast
import com.example.ut4android.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteListFragment : BaseFragment(R.layout.fragment_favorite_list) {

    private lateinit var binding: FragmentFavoriteListBinding
    private val viewModel by activityViewModels<FavoriteViewModel>()
    private lateinit var adapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteListBinding.bind(view)
        binding.floatingButton.setOnClickListener(this::addFavorite)
        adapter = FavoriteAdapter(layoutInflater) { isLongClick, article ->
            onItemClick(isLongClick, article)
        }
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteListFragment.adapter
        }

        subscribeBasic(viewModel)
        viewModel.favorites.observe(viewLifecycleOwner) {
            onFavoritesChange(it)
        }

        viewModel.loadFavoriteArticleList(0)
    }

    private fun onItemClick(isLongClick: Boolean, article: ArticleEntity) {
        if (isLongClick) {
            showRemoveFavoriteDialog(article)
        } else {
            if (article.link.isNotEmpty() && article.link.startsWith("http")) {
                val intent = Intent(Intent.ACTION_VIEW, article.link.toUri())
                startActivity(intent)
            } else {
                toast("not support!")
            }
        }
    }

    private fun showRemoveFavoriteDialog(article: ArticleEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Do you want to remove it from your favorites?")
            .setPositiveButton("ok") { d, _ ->
                viewModel.removeFavoriteArticle(article.gid, article.originId)
                d.dismiss()
            }
            .setNegativeButton("cancel") { d, _ ->
                d.dismiss()
            }
            .create()
            .show()
    }

    override fun onLoadingStateChange(show: Boolean) {
        binding.progress.isVisible = show
    }

    private fun onFavoritesChange(list: List<ArticleEntity>) {
        adapter.setNewData(list)
    }

    private fun addFavorite(view: View) {
        parentFragmentManager.commit {
            replace(R.id.fragment_container, AddFavoriteFragment())
                .addToBackStack(AddFavoriteFragment::class.java.simpleName)
        }
    }
}

private class FavoriteAdapter(
    private val inflater: LayoutInflater,
    private val onClick: (isLongClick: Boolean, ArticleEntity) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val data: ArrayList<ArticleEntity> = ArrayList()

    fun setNewData(list: List<ArticleEntity>?) {
        data.clear()
        list?.let {
            data.addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val b = ItemFavoriteBinding.inflate(inflater, parent, false)
        return ViewHolder(b).apply {
            b.root.setOnLongClickListener {
                onClick(true, data[bindingAdapterPosition])
                true
            }
            b.root.setOnClickListener {
                onClick(false, data[bindingAdapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = data[position]
        val b = holder.b
        b.title.text = article.title
        b.author.text = article.author
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(val b: ItemFavoriteBinding) : RecyclerView.ViewHolder(b.root)
}