package com.pru.navapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

class BasePagingSource<V : Any>(
    private val totalPages: Int? = null,
    private val block: suspend (Int, Int) -> List<V>
) : PagingSource<Int, V>() {
    private val FIRST_PAGE = 0
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        val page = params.key ?: FIRST_PAGE
        return try {
            val response = block(page, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (page == FIRST_PAGE) null else page - 1,
                nextKey = if (totalPages != null && page == totalPages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}