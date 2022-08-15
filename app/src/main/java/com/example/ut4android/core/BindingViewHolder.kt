package com.example.ut4android.core

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/8/12
 */
class BindingViewHolder<VB : ViewBinding>(val b: VB) : RecyclerView.ViewHolder(b.root)