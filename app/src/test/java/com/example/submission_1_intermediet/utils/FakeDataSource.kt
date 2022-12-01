package com.example.submission_1_intermediet.utils

import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.remote.response.LoginResponse
import com.example.submission_1_intermediet.data.remote.response.LoginResult
import com.example.submission_1_intermediet.data.remote.response.StoriesResponse
import com.example.submission_1_intermediet.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeDataSource : ApiService {

    private val dummyNews = DataDummy.generateDummyStory()

    override suspend fun setUserRegister(
        name: String,
        email: String,
        password: String
    ): ApiResponse {
        return ApiResponse(
            false,
            "berhasil"
        )
    }

    override suspend fun setUserLogin(email: String, password: String): LoginResponse {
        return LoginResponse(
            loginResult = LoginResult(name = "Harvey", userId = "1234", token = "1234jsad"),
            error = false,
            message = "berhasil")
    }

    override suspend fun setAddStory(
        token: String,
        description: RequestBody,
        file: MultipartBody.Part
    ): ApiResponse {
        return ApiResponse(false, "berhasil")
    }

    override suspend fun getStories(token: String, page: Int, size: Int): StoriesResponse {
        return dummyNews
    }

    override suspend fun getStoriesLocation(token: String): StoriesResponse {
        return dummyNews
    }
}