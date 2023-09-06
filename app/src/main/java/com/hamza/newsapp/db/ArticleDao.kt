package com.hamza.newsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hamza.newsapp.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertArticle(article: Article): Long

    @Query("SELECT * From articles")
    fun getAllArticle(): LiveData<List<Article>>
    //not suspend because it will return a live data and that not work with suspend

    @Delete
    suspend fun deleteArticle(article: Article)
}