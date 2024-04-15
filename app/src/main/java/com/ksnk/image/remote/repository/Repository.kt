package com.ksnk.image.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ksnk.image.DataItem
import com.ksnk.image.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val remoteDataSource: RemoteDataSource) {

    private val fileInfoMutableLiveData = MutableLiveData<List<DataItem>>()

    fun fileInfoModelLiveData(): LiveData<List<DataItem>> =
        fileInfoMutableLiveData

    suspend fun getApiData() = withContext(Dispatchers.IO) {
        runCatching {
            fileInfoMutableLiveData.postValue(
                 remoteDataSource.responseBody()
            )
        }.onSuccess {
            //Timber.d("Api data send in postValue")
        }.onFailure {
          //  Timber.e(it)
        }
    }
}