package com.ksnk.image.remote

import com.ksnk.image.DataItem
import retrofit2.http.GET

interface Api {

    @GET("usa_images.ksnk")
    suspend fun getApi(): List<DataItem>

}