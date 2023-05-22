package com.sushmita.downloadimage.network

interface ApiCallListener {
    fun onSuccess(response: String, requestUrl: String)
    fun onError(response: String, requestUrl: String)
}