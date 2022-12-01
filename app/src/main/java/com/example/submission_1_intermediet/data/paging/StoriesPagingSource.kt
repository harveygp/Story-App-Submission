package com.example.submission_1_intermediet.data.paging

import android.content.SharedPreferences
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem
import com.example.submission_1_intermediet.data.remote.retrofit.ApiService
import javax.inject.Inject

class StoriesPagingSource @Inject constructor(
    private val api : ApiService,
    private val prefs : SharedPreferences
) : PagingSource<Int, ListStoryItem>() {

    private val _pref = prefs.getString("Token", null)

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = api.getStories("Bearer $_pref", position, params.loadSize)
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}