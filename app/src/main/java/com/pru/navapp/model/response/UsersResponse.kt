package com.pru.navapp.model.response


import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("data")
    val users: List<User>? = null,
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("per_page")
    val perPage: Int? = null,
    @SerializedName("support")
    val support: Support? = null,
    @SerializedName("total")
    val total: Int? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null
)