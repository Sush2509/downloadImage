package com.sushmita.downloadimage.application

import android.Manifest
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.sushmita.downloadimage.R
import com.sushmita.downloadimage.utils.AppCons
import com.sushmita.downloadimage.utils.AppUtils
import com.sushmita.downloadimage.utils.MyDownloadManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    private val channelId = "i.apps.notifications"
    private var context : Context? =  null

    override fun onCreate() {
        super.onCreate()
        context = this
        MyDownloadManager.getInstance().init(this)
        MyDownloadManager.getInstance().getRequestLiveData().observeForever {
            for(key in it.keys){
                val innerMap = it[key]
                if(innerMap!=null){
                    val allDone = innerMap.values.stream().allMatch { n -> n == true }
                    if(allDone){
                        it.remove(key)
                        showNotification()
                    }
                }
            }

        }
    }

    private fun showNotification(){
        createNotificationChannel()
        sendNotification()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification title"
            val desTexxt = "Notification Desc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId,name,importance).apply {
                description = desTexxt
            }
            val notiManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notiManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this,channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Download")
            .setContentText("Download Completed")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            if (ActivityCompat.checkSelfPermission(context!!,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(AppCons.NOTIFICATION_ID,builder.build())
        }
    }

}