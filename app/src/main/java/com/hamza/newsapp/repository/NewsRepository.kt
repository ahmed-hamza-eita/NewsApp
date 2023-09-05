package com.hamza.newsapp.repository

import com.hamza.newsapp.db.ArticleDB
import com.hamza.newsapp.network.ApiCalls
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val apiCalls: ApiCalls,
    private val localDB: ArticleDB
) {

    suspend fun getBreakingNews( countryCode:String, pageNumber: Int) =
        apiCalls.getBreakingNews( countryCode, pageNumber)
}