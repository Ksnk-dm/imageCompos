package com.ksnk.image.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {

    @GET("api/")
    suspend fun getApi(): Response<ResponseBody>

}