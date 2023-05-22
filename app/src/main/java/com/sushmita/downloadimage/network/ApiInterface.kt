package com.sushmita.downloadimage.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET(ApiConstants.API_END_POINT.IMAGES)
    fun getImageList(): Call<ResponseBody>
}