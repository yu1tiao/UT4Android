package com.example.ut4android.util

import android.util.Log
import com.example.ut4android.appContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Copyright (c) Spond All rights reserved.
 * @author leo
 * @date 2022/7/15
 */
object JacocoHelper {

    private const val TAG = "JacocoHelper"

    private val DEFAULT_COVERAGE_FILE_PATH: String = appContext().filesDir.path + "/coverage.ec"
//    private val DEFAULT_COVERAGE_FILE_PATH: String = Environment.getExternalStorageDirectory().path + "/coverage.ec"

    fun generateEcFile(createNewFile: Boolean) {
        var out: OutputStream? = null
        val mCoverageFilePath = File(DEFAULT_COVERAGE_FILE_PATH)
        try {
            if (createNewFile && mCoverageFilePath.exists()) {
                mCoverageFilePath.delete()
            }
            if (!mCoverageFilePath.exists()) {
                mCoverageFilePath.createNewFile()
            }
            out = FileOutputStream(mCoverageFilePath.path, true)
            val agent = Class.forName("org.jacoco.agent.rt.RT")
                .getMethod("getAgent")
                .invoke(null)
            if (agent != null) {
                val method = agent.javaClass.getMethod(
                    "getExecutionData",
                    Boolean::class.javaPrimitiveType
                )
                out.write(method.invoke(agent, false) as ByteArray)
                Log.i(TAG, "generate file success, file path is : $DEFAULT_COVERAGE_FILE_PATH")
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}