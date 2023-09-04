package com.hamza.newsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hamza.newsapp.models.Article
import com.hamza.newsapp.utils.Const

@Database(
    entities = [Article::class], version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDB : RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao

    companion object {
        @Volatile
        private var instance: ArticleDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDB::class.java,
                Const.DB_NAME
            ).build()
    }
}