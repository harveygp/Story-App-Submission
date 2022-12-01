package com.example.submission_1_intermediet.data.remote.retrofit

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.submission_1_intermediet.BuildConfig
import com.example.submission_1_intermediet.data.paging.StoriesPagingSource
import com.example.submission_1_intermediet.data.repository.UserRepositoryImpl
import com.example.submission_1_intermediet.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiConfig {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPref(app : Application) : SharedPreferences =
        app.getSharedPreferences("prefs", MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideUserRepository(api : ApiService, prefs : SharedPreferences) : UserRepository = UserRepositoryImpl(api, prefs)

    @Provides
    @Singleton
    fun provideStoriesPagingSource(api : ApiService, prefs : SharedPreferences) = StoriesPagingSource(api, prefs)

}