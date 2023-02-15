package com.example.news.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.news.data.local.NewsDatabase
import com.example.news.data.paging.NewsPagingSource
import com.example.news.data.paging.SearchPagingSource
import com.example.news.data.remote.NewsApi
import com.example.news.models.Article
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@ExperimentalPagingApi
class Repository @Inject constructor(
    private val newsApi: NewsApi,
    private val database: NewsDatabase
) {
    fun getCategoryResults(query: String) =
    // Pager class responsible for producing the PagingData stream
        // .It depends on the PagingSource to do this
        Pager(
            // PagingConfig is a class defines the parameters that determine paging behavior.
            //  This includes page size, whether placeholders are enabled, and so on.
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
            ),
            // The pagingSourceFactory lambda
            // should always return a brand new PagingSource when invoked
            // as PagingSource instances are not reusable.
            // provides an instance of the UnsplashPagingSource we just created.
            pagingSourceFactory = { NewsPagingSource(newsApi, query) }
            //PagingData is a type that wraps the data we've loaded
            // and helps the Paging library decide when to fetch more data,
            // and also make sure we don't request the same page twice
            // PagingData here is wrapped with liveData -->  LiveData<PagingData<UnsplashPhoto>>
        ).liveData

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { SearchPagingSource(newsApi, query) }
        ).liveData


    suspend fun saveArticle(article: Article) = database.newsDao().insert(article)

    suspend fun deleteArticle(article: Article) = database.newsDao().delete(article)

    fun getSavedArticles() = database.newsDao().getArticles()


}