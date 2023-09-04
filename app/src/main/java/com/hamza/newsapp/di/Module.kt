package com.hamza.newsapp.di

import android.content.Context
import androidx.room.Room
import com.hamza.newsapp.db.ArticleDB
import com.hamza.newsapp.db.ArticleDao
import com.hamza.newsapp.network.ApiCalls
import com.hamza.newsapp.utils.Const
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun retrofitConnection(): ApiCalls {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder().baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiCalls::class.java)

    }

    //Room
    /*
    @Singleton
    @Provides
    fun provideRoomDB(@ApplicationContext context: Context): ArticleDao {
        val myDataBase = Room.databaseBuilder(context, ArticleDB::class.java, Const.DB_NAME)
            .fallbackToDestructiveMigration().build()
        return myDataBase.getArticleDao()
    }
*/
    @Provides
    @Singleton
    fun provideArticleDatabase(@ApplicationContext context: Context): ArticleDB {
        return ArticleDB.invoke(context)
    }
}