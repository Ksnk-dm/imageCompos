package com.ksnk.image.remote

import com.ksnk.image.remote.model.DataModel
import retrofit2.http.GET

interface Api {

    @GET("images/liver_images")
    suspend fun getApi(): List<DataModel>

}