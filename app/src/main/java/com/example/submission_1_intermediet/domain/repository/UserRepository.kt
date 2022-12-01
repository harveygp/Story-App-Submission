package com.example.submission_1_intermediet.domain.repository

import androidx.paging.PagingData
import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem
import com.example.submission_1_intermediet.data.remote.response.LoginResponse
import com.example.submission_1_intermediet.data.remote.response.StoriesResponse
import com.example.submission_1_intermediet.utils.Condition
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {

    suspend fun getStories() : Flow<PagingData<ListStoryItem>>

    suspend fun getStoriesLocation(token : String?) : Flow<Condition<StoriesResponse>>

    suspend fun setUserRegister(name : String?, email : String, password : String ) : Flow<Condition<ApiResponse>>

    suspend fun setUserLogin(email : String?, password : String ) : Flow<Condition<LoginResponse>>

    suspend fun setAddStory(token : String?, description : String, photo : File) : Flow<Condition<ApiResponse>>

    fun deleteTokenandSesi() : Flow<Condition<Boolean>>

}