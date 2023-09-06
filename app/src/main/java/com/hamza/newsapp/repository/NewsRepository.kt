package com.hamza.newsapp.repository

import com.hamza.newsapp.db.ArticleDB
import com.hamza.newsapp.db.ArticleDao
import com.hamza.newsapp.models.Article
import com.hamza.newsapp.network.ApiCalls
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val apiCalls: ApiCalls,
    private val localDB: ArticleDB
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        apiCalls.getBreakingNews(countryCode, pageNumber)

    suspend fun searchForNew(searchTerm: String, pageNumber: Int) =
        apiCalls.searchForNews(searchTerm, pageNumber)

    suspend fun upsertArticle(article: Article) = localDB.getArticleDao().upsertArticle(article)
    fun getSavedNews() = localDB.getArticleDao().getAllArticle()
    suspend fun deleteArticle(article: Article) = localDB.getArticleDao().deleteArticle(article)


}

