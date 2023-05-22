package com.sushmita.downloadimage.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sushmita.downloadimage.model.helperclasses.DownloadAdapterItemModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.math.roundToInt


class MyDownloadManager {

    var context: Context? = null
    private var reqLiveData = MutableLiveData<MutableMap<String, MutableMap<String, Boolean>>>()

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: MyDownloadManager? = null

        fun getInstance(): MyDownloadManager {
            return INSTANCE ?: synchronized(this) {
                val instance = MyDownloadManager()
                INSTANCE = instance
                instance
            }
        }
    }

    fun init(context: Context) {
        this.context = context
        reqLiveData.value = mutableMapOf()
    }

    fun getRequestLiveData() : MutableLiveData<MutableMap<String, MutableMap<String, Boolean>>> {
        return reqLiveData
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun download(requestId: String, urlList: MutableList<DownloadAdapterItemModel>?) {
        GlobalScope.async(Dispatchers.IO) {
            if (urlList != null) {
                reqLiveData.value?.put(requestId,mutableMapOf())
                for(url in urlList){
                    downloadFile(requestId,url.imgURL)
                }
            }
        }
    }

    private fun downloadFile(requestId: String, url:String?){
        val fileName = File(url!!).name
        val file = File(commonDocumentDirPath("DogImages"), fileName)
        downloadFile(requestId,url,file)
    }

    private fun commonDocumentDirPath(folderName: String): File? {
        var dir: File?
        dir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/" + folderName
            )
        } else {
            File(Environment.getExternalStorageDirectory().toString() + "/" + folderName)
        }
        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            val success = dir.mkdirs()
            if (!success) {
                dir = null
            }
        }
        return dir
    }

    private fun downloadFile(requestId: String,url: String, file: File) {
        val ktor = HttpClient(Android)
        FileOutputStream(file).let { outputStream ->
            CoroutineScope(Dispatchers.IO).launch {
                ktor.downloadFile(outputStream, url).collect {
                    withContext(Dispatchers.Main) {
                        when (it) {
                            is DownloadResult.Success -> {
                                reqLiveData.value?.get(requestId)?.put(url,true)
                                reqLiveData.value = reqLiveData.value
                                Log.i("IMG_DOWNLOAD","SUCCESS requestId: $requestId, url: $url, file: $file")
                            }
                            is DownloadResult.Error -> {
                                reqLiveData.value?.get(requestId)?.put(url,true)
                                reqLiveData.value = reqLiveData.value
                                Log.i("IMG_DOWNLOAD","ERROR requestId: $requestId, url: $url, file: $file")
                            }
                            is DownloadResult.Progress -> {

                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun HttpClient.downloadFile(file: OutputStream, url: String): Flow<DownloadResult> {
        return flow {
            try {
                val response = call {
                    url(url)
                    method = HttpMethod.Get
                }.response

                val data = ByteArray(response.contentLength()!!.toInt())
                var offset = 0

                do {
                    val currentRead = response.content.readAvailable(data, offset, data.size)
                    offset += currentRead
                    val progress = (offset * 100f / data.size).roundToInt()
                    emit(DownloadResult.Progress(progress))
                } while (currentRead > 0)

                response.close()

                if (response.status.isSuccess()) {
                    withContext(Dispatchers.IO) {
                        file.write(data)
                        file.close()
                    }
                    emit(DownloadResult.Success)
                } else {
                    emit(DownloadResult.Error("File not downloaded"))
                }
            } catch (e: TimeoutCancellationException) {
                emit(DownloadResult.Error("Connection timed out", e))
            } catch (t: Throwable) {
                emit(DownloadResult.Error("Failed to connect"))
            }
        }
    }
}