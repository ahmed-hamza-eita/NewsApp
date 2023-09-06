package com.hamza.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamza.newsapp.models.Article
import com.hamza.newsapp.models.NewsResponse
import com.hamza.newsapp.repository.NewsRepository
import com.hamza.newsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repo: NewsRepository) : ViewModel() {

    val breakingNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse:NewsResponse? = null

    val searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse:NewsResponse? = null


    init {
        getBreakingNews("eg")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNewsLiveData.postValue(Resource.Loading())
        val response = repo.getBreakingNews(countryCode, breakingNewsPage)
        breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchTerm: String) = viewModelScope.launch {
        searchNewsLiveData.postValue(Resource.Loading())
        val response = repo.searchForNew(searchTerm, searchNewsPage)
        searchNewsLiveData.postValue(handleSearchNewsResponse(response))
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=result
                }else{
                    val oldArticle=breakingNewsResponse?.articles
                    val newArticle=result.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse?:result)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                searchNewsPage++
                if(searchNewsResponse==null){
                    searchNewsResponse=result
                }else{
                    val oldArticle=searchNewsResponse?.articles
                    val newArticle=result.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(searchNewsResponse?:result)
            }
        }
        return Resource.Error(response.message())


    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repo.upsertArticle(article)
    }

    fun getSavedNews() = repo.getSavedNews()
    fun deleteArticle(article: Article) = viewModelScope.launch {
        repo.deleteArticle(article)
    }
}