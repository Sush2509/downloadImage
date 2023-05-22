package com.sushmita.downloadimage.utils

import android.util.Log
import com.intuit.sdp.BuildConfig

class AppLogger {
    companion object {
        private const val DEBUG = BuildConfig.DEBUG

        fun d(tag: String, msg: String) {
            if (DEBUG) {
                Log.d(tag, "" + msg)
            }
        }
        fun e(tag: String, msg: String?) {
            if (DEBUG) {
                Log.e(tag, "" + msg)
            }
        }
    }
}