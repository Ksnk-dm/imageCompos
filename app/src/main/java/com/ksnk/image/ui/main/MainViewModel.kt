package com.ksnk.image.ui.main

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ksnk.image.R
import com.ksnk.image.remote.model.DataModel
import com.ksnk.image.remote.repository.Repository
import com.ksnk.image.ui.main.MainActivity.Companion.FILE.ENVIRONMENT
import com.ksnk.image.ui.main.MainActivity.Companion.FILE.FORMAT
import com.ksnk.image.ui.main.MainActivity.Companion.FILE.MIME_TYPE
import com.ksnk.image.ui.main.MainActivity.Companion.FILE.TITLE
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val repository: Repository,
    private val application: Application
) : AndroidViewModel(application) {

    private val urlLiveData = repository.fileInfoModelLiveData()

    fun urlLiveData(): LiveData<List<DataModel>> =
        urlLiveData

    fun loadDataFromUrl() {
        viewModelScope.launch {
            repository.getApiData()
        }
    }

    fun downloadFile(fileInfo: String, context: Context): File? = runCatching {
        if (!ENVIRONMENT.exists()) {
            ENVIRONMENT.mkdirs()
        }
        val dm = ContextCompat.getSystemService(context, DownloadManager::class.java)
        val downloadUri = Uri.parse(fileInfo)
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle(TITLE)
            .setMimeType(MIME_TYPE)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(
                Uri.fromFile(
                    File(
                        ENVIRONMENT,
                        "${System.currentTimeMillis()}$FORMAT"
                    )
                )
            )

        dm?.enqueue(request)

        Toast.makeText(context, context.getText(R.string.start), Toast.LENGTH_SHORT).show()
        File(ENVIRONMENT, "${System.currentTimeMillis()}$FORMAT")
    }.onFailure { e ->
        Toast.makeText(context, "${context.getText(R.string.failed)} ${e.message}", Toast.LENGTH_SHORT).show()
    }.getOrNull()
}