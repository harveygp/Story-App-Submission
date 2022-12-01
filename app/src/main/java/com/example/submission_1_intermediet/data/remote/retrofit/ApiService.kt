package com.example.submission_1_intermediet.data.remote.retrofit

import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.remote.response.LoginResponse
import com.example.submission_1_intermediet.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("v1/register")
    suspend fun setUserRegister(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : ApiResponse

    @FormUrlEncoded
    @POST("v1/login")
    suspend fun setUserLogin(
        @Field("email") email : String,
        @Field("password") password : String
    ) : LoginResponse

    @Multipart
    @POST("v1/stories")
    suspend fun setAddStory(
        @Header("Authorization") token : String,
        @Part("description") description : RequestBody,
        @Part file: MultipartBody.Part
    ) : ApiResponse

    @GET("v1/stories")
    suspend fun getStories(
        @Header("Authorization") token : String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : StoriesResponse

    @GET("v1/stories?location=1")
    suspend fun getStoriesLocation(
        @Header("Authorization") token : String
    ) : StoriesResponse
}