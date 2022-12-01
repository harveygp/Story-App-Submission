package com.example.submission_1_intermediet.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem


class PagingTesting : PagingSource<Int, ListStoryItem>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return LoadResult.Page(emptyList(),0,1)
    }

    companion object{
        fun snapshot(item: List<ListStoryItem>) : PagingData<ListStoryItem> {
            return PagingData.from(item)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        TODO("Not yet implemented")
    }
}