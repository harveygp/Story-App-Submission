package com.example.submission_1_intermediet.utils

import com.example.submission_1_intermediet.data.remote.response.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object DataDummy {
    fun generateDummyListStory(): List<ListStoryItem> {
        val newsList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val news = ListStoryItem(
                "Title $i",
                "person $i",
                "description $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                i.toDouble(),
                i.toDouble()
            )
            newsList.add(news)
        }
        return newsList
    }

    fun generateDummyStory(): StoriesResponse {
        val newsList1 = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val news1 = ListStoryItem(
                "Title $i",
                "person $i",
                "description $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                i.toDouble(),
                i.toDouble()
            )
            newsList1.add(news1)
        }
        return StoriesResponse(
            error = false,
            message = "masuk",
            listStory = newsList1
        )
    }

    fun generateDummyStoryEmpty(): StoriesResponse {
        val newsList1 = ArrayList<ListStoryItem>()
        return StoriesResponse(
            error = false,
            message = "masuk",
            listStory = newsList1
        )
    }



    fun generateDummyStoryPagingResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..10) {
            val story = ListStoryItem(
                "Title $i",
                "person $i",
                "description $i",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-02-22T22:22:22Z",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }

    fun generateLoginResponse(): LoginResponse {
        return LoginResponse(
            loginResult = LoginResult(name = "Harvey", userId = "1234", token = "1234jsad"),
            error = false,
            message = "berhasil")
    }

    fun generateAddResponse(): ApiResponse {
            return ApiResponse(
                false,"berhasil"
            )
    }
}