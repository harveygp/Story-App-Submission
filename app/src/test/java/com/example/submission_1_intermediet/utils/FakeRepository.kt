package com.example.submission_1_intermediet.utils

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.submission_1_intermediet.data.paging.StoriesPagingSource
import com.example.submission_1_intermediet.data.remote.response.*
import com.example.submission_1_intermediet.data.remote.retrofit.ApiService
import com.example.submission_1_intermediet.data.repository.UserRepositoryImpl
import com.example.submission_1_intermediet.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.mockito.Mock
import java.io.File

class FakeRepository(private val shared : SharedPreferences) : UserRepository{

    private var api: ApiService = FakeDataSource()

    private val dummyNews = DataDummy.generateDummyStory()


    override suspend fun getStories(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoriesPagingSource(api,shared)
            }
        ).flow
    }

    override suspend fun getStoriesLocation(token: String?): Flow<Condition<StoriesResponse>> = flow{
        emit(
            Condition.Success(
                dummyNews
            )
        )
    }

    override suspend fun setUserRegister(
        name: String?,
        email: String,
        password: String
    ): Flow<Condition<ApiResponse>> = flow{
        emit(
            Condition.Success(
                ApiResponse(
                    error = false,
                    message = "berhasil"
                )
            )
        )
    }

    override suspend fun setUserLogin(
        email: String?,
        password: String
    ): Flow<Condition<LoginResponse>> = flow{
        emit(
            Condition.Success(LoginResponse(
                loginResult = LoginResult(
                    name = "harvey",
                    userId = "1234",
                    token = "12345"
                ),
                error = false,
                message = "berhasil"
            )
            )
        )
    }

    override suspend fun setAddStory(
        token: String?,
        description: String,
        photo: File
    ): Flow<Condition<ApiResponse>> = flow{
        emit(
            Condition.Success(
                ApiResponse(
                    error = false,
                    message = "berhasil"
                )
            )
        )
    }

    override fun deleteTokenandSesi(): Flow<Condition<Boolean>> {
        TODO("Not yet implemented")
    }

}