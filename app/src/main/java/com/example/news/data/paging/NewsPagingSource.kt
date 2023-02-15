package com.example.news.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.news.data.remote.NewsApi
import com.example.news.models.Article
import com.example.news.util.Constants.Companion.STARTING_PAGE_INDEX
import okio.IOException
import retrofit2.HttpException

// PagingSource is the base class for loading chunks of data of specific page query
// responsible for loading data from our REST API and the turn it to pages
// we don't use dagger to inject the constructor since query is only known during the runtime not compile time
class NewsPagingSource(
    private val newsApi: NewsApi, // since data will be fetched mainly from the api
    private val query: String
) : PagingSource<Int, Article>() {
    // Type of PagingSource PagingKey is Int , type of LoadedData is UnsplashPhoto
    // PagingSource requires us to implement two functions: load() and getRefreshKey().

    // The load() function will be called by the Paging library
    // to asynchronously fetch more data to be displayed as the user scrolls around.
    // The LoadParams object keeps information related to the load operation including the following:
    // 1-Key of the page to be loaded - If this is the first time that load() is called,
    // LoadParams.key will be null. In this case, you will have to define the initial page key.
    // UNSPLASH_STARTING_PAGE_INDEX used as the key.
    // 2-Load size - the requested number of items to load.

    // The load() function returns a LoadResult. The LoadResult can be one of the following types:
    // a- LoadResult.Page, if the result was successful.
    // b- LoadResult.Error, in case of error.
    // c- LoadResult.Invalid, if the PagingSource should be invalidated
    // because it can no longer guarantee the integrity of its results.


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val start = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response =
                newsApi.getCategory(category = query, page = start, pageSize = params.loadSize).articles
            // A LoadResult.Page has three required arguments:
            // data: A List of the items fetched.
            // prevKey: The key used by the load() method if it needs to fetch items behind the current page.
            // nextKey: The key used by the load() method if it needs to fetch items after the current page.
            LoadResult.Page(
                data = response,
                nextKey = if (response.isNotEmpty()) start + 1 else null,
                prevKey = if (start == STARTING_PAGE_INDEX) null else start - 1
            )
        } catch (e: IOException) { // when there is no internet connection when we make a request
            LoadResult.Error(e)
        } catch (e: HttpException) { // when we make a request but there is something wrong with the server
            LoadResult.Error(e)
        }
    }

    // The refresh key is used for the initial load of the next PagingSource, after invalidation
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}