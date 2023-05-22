package com.sushmita.downloadimage.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AppUtils {
    companion object{
        fun showToast(context: Context?, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun checkAppPermission(context : Context) : Boolean{
            val isPermissionGranted: Boolean
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                {
                    isPermissionGranted = false
                } else {
                    isPermissionGranted = true
                    Log.e("DB", "PERMISSION GRANTED")
                }
            }
            else {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                {
                    isPermissionGranted = false
                } else {
                    isPermissionGranted = true
                    Log.e("DB", "PERMISSION GRANTED")
                }
            }

            return isPermissionGranted
        }

        // Notify user about the persmission status that permission is granted or not
        fun showPermissionIssuePopup(activity: Activity){
            requestPermission(activity)
        }

        private fun requestPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    ), AppCons.DOWNLOAD_PERMISSION_REQUEST_CODE
                )
            }else {
                ActivityCompat.requestPermissions(activity, arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS
                ), AppCons.DOWNLOAD_PERMISSION_REQUEST_CODE)
            }
        }

    }
}