package com.ksnk.image.remote

class RemoteDataSource (private val apiService: Api) {

    suspend fun responseBody() = apiService.getApi()
}