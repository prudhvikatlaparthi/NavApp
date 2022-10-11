package com.pru.navapp.listeners

import com.pru.navapp.utils.Constants.kInitialPage
import com.pru.navapp.utils.Constants.kPageSize

interface Paging {
    var pageIndex: Int
    var isLoading: Boolean
    var isLastPage: Boolean

    fun fetchData()

    fun resetPaging() {
        pageIndex = kInitialPage
        isLastPage = false
        isLoading = false
        fetchData()
    }

    fun getPageSize(): Int {
        return if (pageIndex == kInitialPage) kPageSize else kPageSize
    }
}