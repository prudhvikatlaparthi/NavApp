package com.pru.navapp.repository

import com.pru.navapp.model.response.PassengersResponse
import com.pru.navapp.paging.PagingUtils.createPager
import com.pru.navapp.remote.ApiService
import com.pru.navapp.utils.ApiState
import kotlinx.coroutines.flow.flow

class ApiRepository(private val apiService: ApiService) {

    fun getPassengers() = createPager { page, size ->
        apiService.getPassengers(page = page, size = size).passengerData ?: emptyList()
    }.flow

    suspend fun getPassengersPaging(page: Int, size: Int) = flow<ApiState<PassengersResponse>> {
        emit(ApiState.Loading())
        kotlin.runCatching {
            apiService.getPassengers(page = page, size = size)
        }.onSuccess {
            emit(ApiState.Success(it))
        }.onFailure {
            emit(ApiState.Failure(it.message ?: "Error"))
        }
    }
}