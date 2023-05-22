package com.sushmita.downloadimage.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.sushmita.downloadimage.R
import com.sushmita.downloadimage.model.helperclasses.DownloadAdapterItemModel
import com.sushmita.downloadimage.utils.AppUtils
import com.sushmita.downloadimage.utils.MyDownloadManager
import kotlinx.coroutines.*
import java.util.UUID

class DownloadFragmentVM(application: Application) : AndroidViewModel(application) {

    private var adapterItemList: MutableList<DownloadAdapterItemModel> = ArrayList()
    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext

    fun getAdapterItemList(): MutableList<DownloadAdapterItemModel> {
        return adapterItemList
    }

    // download button to download all the images
    fun onDownloadClick(view: View?) {
        AppUtils.showToast(context,context.getString(R.string.download_started))
        val uuid = UUID.randomUUID().toString()
        MyDownloadManager.getInstance().download(uuid,adapterItemList)
    }

}