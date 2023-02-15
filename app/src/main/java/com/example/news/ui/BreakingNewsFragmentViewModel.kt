package com.example.news.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.example.news.data.repository.Repository
import com.example.news.models.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class BreakingNewsFragmentViewModel @Inject constructor(
    private val repository: Repository,
    state: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val CURRENT_QUERY = "current_query" // just a key to be used by state
        private const val DEFAULT_QUERY = ""
    }


    // to handle process death by saving last search query
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    // photos: LiveData<PagingData<UnsplashPhoto>> depends on the search query
    // search query will be either empty (DEFAULT_QUERY) -> getAllResults
    // or search query will have a value -> getSearchResults(query)
    // after fetching data and cache it in the room database
    val news = currentQuery.switchMap { query ->
        if (query.isNotEmpty())
            repository.getSearchResults(query).cachedIn(viewModelScope)
        else repository.getCategoryResults("").cachedIn(viewModelScope)
    }

    // search function changes the currentQuery.value to the recent search query
    fun searchNews(query: String) {
        currentQuery.value = query
    }

    val techNews = repository.getCategoryResults("technology").cachedIn(viewModelScope)
    val sportsNews = repository.getCategoryResults("sports").cachedIn(viewModelScope)
    val businessNews = repository.getCategoryResults("business").cachedIn(viewModelScope)


    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.saveArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.deleteArticle(article)
    }

    fun getSavedArticles() = repository.getSavedArticles()


}