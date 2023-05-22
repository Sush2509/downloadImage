package com.sushmita.downloadimage.network

import com.sushmita.downloadimage.utils.AppLogger
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitApiCall {

    companion object {
        fun hitApi(
            apiCall: Call<ResponseBody>,
            apiCallListener: ApiCallListener,
            key: String
        ) {
            apiCall.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    AppLogger.e("Api_Url: ", call.request().url.toString())
                    try {
                        val responseBody = response.body()!!.string().trim { it <= ' ' }
                        AppLogger.e("onResponse", responseBody)
                        apiCallListener.onSuccess(responseBody, key)
                    } catch (e: Exception) {
                        AppLogger.e("onResponse_Exception", e.message.toString())
                        apiCallListener.onError(e.message.toString(), key)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    AppLogger.e("onFailure_Exception", t.message.toString())
                    apiCallListener.onError(t.message.toString(), key)
                }
            })
        }
    }
}