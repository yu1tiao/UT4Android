package com.example.ut4android.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ut4android.core.BaseActivity
import com.example.ut4android.core.BindingViewHolder
import com.example.ut4android.databinding.ActivityMainBinding
import com.example.ut4android.databinding.ItemActionBinding
import com.example.ut4android.util.JacocoHelper
import com.example.ut4android.util.toast
import com.example.ut4android.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar(binding.toolbar)
        subscribeBasic(viewModel)
        viewModel.logout.observe(this) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        adapter = ItemAdapter(layoutInflater)
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

        adapter.setNewData(buildItemData())
    }

    private fun buildItemData(): List<Pair<String, View.OnClickListener>> = listOf(
        "网址收藏" to View.OnClickListener { myFavorite() },
        "生成code coverage报告" to View.OnClickListener { generateCodeCoverageFile() },
        "登录" to View.OnClickListener { login() },
        "退出登录" to View.OnClickListener { logout() },
    )

    private fun login() {
        startActivity(LoginActivity.createIntent(this))
    }

    private fun logout() {
        AlertDialog.Builder(this)
            .setMessage("你即将退出登录")
            .setPositiveButton("退出") { d, _ ->
                viewModel.logout()
                d.dismiss()
            }
            .setNegativeButton("取消") { d, _ -> d.dismiss() }
            .create().show()
    }

    private fun myFavorite() {
        startActivity(Intent(this, FavoriteActivity::class.java))
    }

    private fun generateCodeCoverageFile() {
        JacocoHelper.generateEcFile(true)
        toast("生成成功")
    }
}

class ItemAdapter(private val inflater: LayoutInflater) :
    RecyclerView.Adapter<BindingViewHolder<ItemActionBinding>>() {

    private val list = arrayListOf<Pair<String, View.OnClickListener>>()

    fun setNewData(data: List<Pair<String, View.OnClickListener>>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BindingViewHolder<ItemActionBinding> {
        val b = ItemActionBinding.inflate(inflater, parent, false)
        return BindingViewHolder(b).apply {
            b.btn.setOnClickListener {
                list[bindingAdapterPosition].second.onClick(it)
            }
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemActionBinding>, position: Int) {
        holder.b.btn.text = list[position].first
    }

    override fun getItemCount(): Int {
        return list.size
    }
}