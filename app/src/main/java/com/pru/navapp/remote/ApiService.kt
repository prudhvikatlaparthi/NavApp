package com.pru.navapp.remote

import com.pru.navapp.model.response.PassengersResponse
import com.pru.navapp.model.response.UsersResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        //        private const val kBASEURL = "https://api.instantwebtools.net"
        private const val kBASEURL = "https://reqres.in"

        fun getApiService(): ApiService = Retrofit.Builder().baseUrl(kBASEURL)
            .client(
                OkHttpClient.Builder().addInterceptor(
                    interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                ).build()
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build().create(ApiService::class.java)
    }

    @GET("/v1/passenger")
    suspend fun getPassengers(
        @Query("page") page: Int,
        @Query("size") size: Int = 30
    ): PassengersResponse

    @GET("/api/users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") size: Int
    ): UsersResponse
}