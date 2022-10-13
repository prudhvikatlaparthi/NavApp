package com.pru.navapp.model.response


import com.google.gson.annotations.SerializedName

data class Support(
    @SerializedName("text")
    val text: String? = null,
    @SerializedName("url")
    val url: String? = null
)