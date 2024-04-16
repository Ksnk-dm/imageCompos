package com.ksnk.image.ui.main

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ksnk.image.R
import com.ksnk.image.remote.model.DataItem
import com.ksnk.image.remote.repository.Repository
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(
    private val repository: Repository,
    private val application: Application
) : AndroidViewModel(application) {

    private val expandedUrlLiveData = repository.fileInfoModelLiveData()

    fun getExpandedUrlLiveData(): LiveData<List<DataItem>> =
        expandedUrlLiveData

    fun expandShortenedUrl() {
        viewModelScope.launch {
            repository.getApiData()
        }
    }

    fun downloadFile(fileInfo: String, context: Context): File? = runCatching {
        if (!FILE_ENVIRONMENT.exists()) {
            FILE_ENVIRONMENT.mkdirs()
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
                        FILE_ENVIRONMENT,
                        "${System.currentTimeMillis()}$FORMAT"
                    )
                )
            )

        dm?.enqueue(request)

        Toast.makeText(context, context.getText(R.string.start), Toast.LENGTH_SHORT).show()
        File(FILE_ENVIRONMENT, "${System.currentTimeMillis()}$FORMAT")
    }.onFailure { e ->
        Toast.makeText(context, "${context.getText(R.string.failed)} ${e.message}", Toast.LENGTH_SHORT).show()
    }.getOrNull()

    companion object {
        val FILE_ENVIRONMENT = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "USA IMAGES/IMAGES"
        )

        const val TITLE = "Download"
        const val MIME_TYPE = "jpg/*"
        const val FORMAT = ".jpg"
    }
}