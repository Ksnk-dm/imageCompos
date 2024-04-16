package com.ksnk.image.remote.model

import com.google.gson.annotations.SerializedName

data class DataItem(
    @SerializedName("url")
    val url: String?,
)
