package com.hamza.newsapp.utils

import retrofit2.http.Query

class Const {
    companion object {
        const val BASE_URL = "https://newsapi.org/"
        const val API_KEY = "44e24767d3774d8c983f772b70c3fac4"
        const val DB_NAME = "article_db.db"
        const val SEARCH_NEWS_TIME_DELAY = 1000L
        const val SERIALIZABLE_KEY = "article"
        const val QUERY_PAGE_SIZE = 230
    }
}