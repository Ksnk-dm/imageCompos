package com.ksnk.image

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {

    fun downloadFile(fileInfo: String, context: Context): File? = runCatching {
        if (!File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "USA IMAGES/IMAGES"
            ).exists()) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "USA IMAGES/IMAGES"
            ).mkdirs()
        }
        val dm = ContextCompat.getSystemService(context, DownloadManager::class.java)
        val downloadUri = Uri.parse(fileInfo)
        val request = DownloadManager.Request(downloadUri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            .setAllowedOverRoaming(false)
            .setTitle("download")
            .setMimeType("jpg/*")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationUri(Uri.fromFile(File( File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "USA IMAGES/IMAGES"
            ), "${System.currentTimeMillis()}.jpg")))

        dm?.enqueue(request)

        Toast.makeText(context, "Download started!", Toast.LENGTH_SHORT).show()
        File(File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "USA IMAGES/IMAGES"
        ),"$fileInfo.jpg")
    }.onFailure { e ->
        Toast.makeText(context, "Download failed! ${e.message}", Toast.LENGTH_SHORT).show()
    }.getOrNull()
}