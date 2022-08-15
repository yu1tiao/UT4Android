package com.example.ut4android.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import kotlin.math.abs


/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/3/30
 */
object KeyboardUtil {
    private const val TAG_ON_GLOBAL_LAYOUT_LISTENER = -8
    private var sDecorViewDelta = 0

    fun showKeyboard(view: View) {
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(view, 0)
    }

    fun hideKeyboard(view: View) {
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun isKeyboardVisible(activity: Activity): Boolean {
        return getDecorViewInvisibleHeight(activity.window) > 0
    }

    /**
     * Register soft input changed listener.
     *
     * @param window The window.
     * @param listener The soft input changed listener.
     */
    @JvmStatic
    fun registerSoftInputChangedListener(window: Window, listener: OnSoftInputChangedListener) {
        val flags = window.attributes.flags
        if (flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS != 0) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        val contentView = window.findViewById<View>(android.R.id.content)
        val decorViewInvisibleHeightPre = intArrayOf(getDecorViewInvisibleHeight(window))
        val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val height = getDecorViewInvisibleHeight(window)
            if (decorViewInvisibleHeightPre[0] != height) {
                listener.onSoftInputChanged(height > 0)
                decorViewInvisibleHeightPre[0] = height
            }
        }
        contentView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
        contentView.setTag(TAG_ON_GLOBAL_LAYOUT_LISTENER, onGlobalLayoutListener)
    }

    /**
     * Unregister soft input changed listener.
     *
     * @param window The window.
     */
    @JvmStatic
    fun unregisterSoftInputChangedListener(window: Window) {
        val contentView = window.findViewById<View>(android.R.id.content)
        val tag = contentView.getTag(TAG_ON_GLOBAL_LAYOUT_LISTENER)
        if (tag is ViewTreeObserver.OnGlobalLayoutListener) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                contentView.viewTreeObserver.removeOnGlobalLayoutListener(tag)
                contentView.setTag(TAG_ON_GLOBAL_LAYOUT_LISTENER, null)
            }
        }
    }

    private fun getDecorViewInvisibleHeight(window: Window): Int {
        val decorView = window.decorView
        val outRect = Rect()
        decorView.getWindowVisibleDisplayFrame(outRect)
        val delta: Int = abs(decorView.bottom - outRect.bottom)
        if (delta <= getNavBarHeight(window.context) + getStatusBarHeight(window.context)) {
            sDecorViewDelta = delta
            return 0
        }
        return delta - sDecorViewDelta
    }

    private fun getStatusBarHeight(context: Context): Int {
        val res = context.resources
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    private fun getNavBarHeight(context: Context): Int {
        val res = context.resources
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) {
            res.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    interface OnSoftInputChangedListener {
        fun onSoftInputChanged(isShown: Boolean)
    }
}