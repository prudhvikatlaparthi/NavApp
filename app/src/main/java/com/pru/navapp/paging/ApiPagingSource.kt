package com.pru.navapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pru.navapp.model.response.PassengerData
import com.pru.navapp.remote.ApiService

class ApiPagingSource(private val apiService: ApiService) : PagingSource<Int, PassengerData>() {
    private val FIRST_PAGE = 1;
    override fun getRefreshKey(state: PagingState<Int, PassengerData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PassengerData> {
        return try {
            val page = params.key ?: FIRST_PAGE
            val response = apiService.getPassengers(page = page, size = params.loadSize)
            val data = response.passengerData ?: emptyList()
            LoadResult.Page(
                data = data,
                prevKey = if (page == FIRST_PAGE) null else page - 1,
                nextKey = if (data.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}