package com.pru.navapp.model.response


import com.google.gson.annotations.SerializedName

data class PassengersResponse(
    @SerializedName("data")
    val passengerData: List<PassengerData>? = null,
    @SerializedName("totalPages")
    val totalPages: Int? = null,
    @SerializedName("totalPassengers")
    val totalPassengers: Int? = null
)