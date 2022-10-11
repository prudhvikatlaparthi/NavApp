package com.pru.navapp.model.response


import com.google.gson.annotations.SerializedName

data class PassengerData(
    @SerializedName("airline")
    val airline: List<Airline>? = null,
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("trips")
    val trips: Int? = null,
    @SerializedName("__v")
    val v: Int? = null
)