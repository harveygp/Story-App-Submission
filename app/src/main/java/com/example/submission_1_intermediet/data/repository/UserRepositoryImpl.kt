package com.example.submission_1_intermediet.data.repository

import android.content.SharedPreferences
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submission_1_intermediet.data.paging.StoriesPagingSource
import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem
import com.example.submission_1_intermediet.data.remote.response.LoginResponse
import com.example.submission_1_intermediet.data.remote.response.StoriesResponse
import com.example.submission_1_intermediet.data.remote.retrofit.ApiService
import com.example.submission_1_intermediet.domain.repository.UserRepository
import com.example.submission_1_intermediet.utils.Condition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.lang.NullPointerException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api : ApiService,
    private val prefs : SharedPreferences
) : UserRepository {
    override suspend fun getStories(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(api,prefs)
            }
        ).flow
    }

    override suspend fun getStoriesLocation(token : String?): Flow<Condition<StoriesResponse>> = flow{
        try {
            emit(Condition.Loading())
            val response = api.getStoriesLocation("Bearer ${token!!}")
            emit(Condition.Success(response))
        }catch(e : HttpException){
            emit(Condition.Error(e.localizedMessage ?: "An Error Occurred"))
        }catch (e : NullPointerException){
            emit(Condition.Error(e.localizedMessage ?: "An Error Occurred"))
        }
    }

    override suspend fun setUserRegister(
        name: String?,
        email: String,
        password: String
    ): Flow<Condition<ApiResponse>> = flow{
        try {
            emit(Condition.Loading())
            val response = api.setUserRegister(
                name!!, email, password
            )
            emit(Condition.Success(response))
        }catch(e : HttpException){
            emit(Condition.Error(e.localizedMessage ?: "An Error Occurred"))
        }catch (e : NullPointerException){
            emit(Condition.Error(e.localizedMessage ?: "An Error Occurred"))
        }
    }

    override suspend fun setUserLogin(
        email: String?,
        password: String
    ): Flow<Condition<LoginResponse>> = flow{
        try {
            emit(Condition.Loading())
            val response = api.setUserLogin(
                email!!, password
            )
            emit(Condition.Success(response))
        }catch(e : HttpException){
            emit(Condition.Error(e.localizedMessage ?: "An Error Occurred"))
        }catch (e : NullPointerException){
            emit(Condition.Error(e.localizedMessage ?: "An Error Occurred"))
        }
    }

    override suspend fun setAddStory(
        token: String?,
        description: String,
        photo: File
    ): Flow<Condition<ApiResponse>> = flow{
        try {
            if(token == "kosong") throw NullPointerException("error")
            emit(Condition.Loading())
            val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultiPart : MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                photo.name,
                requestImageFile
            )
            val response = api.setAddStory(
                "Bearer ${token!!}",
                description.toRequestBody("text/plain".toMediaType()),
                imageMultiPart
            )
            emit(Condition.Success(response))
        }catch(e : HttpException){
            emit(Condition.Error(e.localizedMessage ?: "An Error Occurred"))
        }catch (e : NullPointerException){
            emit(Condition.Error(e.localizedMessage ?: "An Error Occurred"))
        }
    }

    override fun deleteTokenandSesi(): Flow<Condition<Boolean>> = flow{
        emit(Condition.Loading())
        try {
            prefs.edit()
                .remove("Sesi")
                .remove("Token")
                .apply()
            emit(Condition.Success(false))
        }catch (e : NullPointerException){
            emit(Condition.Error("Kosong",true))
        }
    }
}