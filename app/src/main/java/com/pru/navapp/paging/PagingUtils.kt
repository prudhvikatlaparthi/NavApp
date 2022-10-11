package com.pru.navapp.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig

object PagingUtils {

    private const val DEFAULT_PAGE_SIZE = 10

    fun <V : Any> createPager(
        totalPages: Int? = null,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        enablePlaceholders: Boolean = false,
        block: suspend (Int, Int) -> List<V>
    ): Pager<Int, V> = Pager(
        config = PagingConfig(enablePlaceholders = enablePlaceholders, pageSize = pageSize),
        pagingSourceFactory = { BasePagingSource(totalPages, block) }
    )
}