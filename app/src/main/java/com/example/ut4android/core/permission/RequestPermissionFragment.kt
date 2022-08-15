package com.example.ut4android.core.permission

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.ut4android.core.permission.PermissionManager

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/26
 */
class RequestPermissionFragment : Fragment() {

    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            onRequestResult(it)
        }
    }

    fun requestPermissions(permissions: Array<String>) {
        permissionsLauncher.launch(permissions)
    }

    private fun onRequestResult(result: Map<String, Boolean>) {
        PermissionManager.handleRequestResult(result)
        detach()
    }

    private fun detach() {
        if (parentFragment != null) {
            requireParentFragment().childFragmentManager.beginTransaction()
                .detach(this@RequestPermissionFragment)
                .remove(this@RequestPermissionFragment)
                .commit()
        } else if (activity != null) {
            requireActivity().supportFragmentManager.beginTransaction()
                .detach(this@RequestPermissionFragment)
                .remove(this@RequestPermissionFragment)
                .commit()
        }
    }
}