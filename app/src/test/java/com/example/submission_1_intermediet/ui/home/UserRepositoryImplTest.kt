package com.example.submission_1_intermediet.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingSource
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.example.submission_1_intermediet.adapter.ListStoriesAdapter
import com.example.submission_1_intermediet.data.paging.StoriesPagingSource
import com.example.submission_1_intermediet.data.remote.retrofit.ApiService
import com.example.submission_1_intermediet.data.repository.UserRepositoryImpl
import com.example.submission_1_intermediet.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.File


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repo : UserRepositoryImpl

    private lateinit var api: ApiService

    @Mock
    private lateinit var pref: SharedPreferences

    @Before
    fun setUp() {
        api = FakeDataSource()
        repo = UserRepositoryImpl(api, pref)
    }

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }


    @Test
    fun`when getStories paging data repo not Null and has value`() = runBlocking {

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        val job = launch(UnconfinedTestDispatcher()){
            repo.getStories().collect{
                differ.submitData(it)
            }
        }
        Assert.assertEquals(DataDummy.generateDummyStoryPagingResponse(),differ.snapshot())
        Assert.assertNotNull(differ.snapshot())
        job.cancel()
    }

    @Test
    fun`when getStoriesLocation repo has value and not null and return success`() = runTest {
            repo.getStoriesLocation("1234jsad").test {
                awaitItem()
                val actual = awaitItem()
                Assert.assertEquals(DataDummy.generateDummyListStory(), actual.data?.listStory)
                Assert.assertNotNull(actual.data)
                Assert.assertTrue(actual is Condition.Success)
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun`when getLogin repo has value and not null and return success`() = runTest {
            repo.setUserLogin("harvey.narukami@gmail.com","124567").test {
                awaitItem()
                val actual = awaitItem()
                Assert.assertEquals(DataDummy.generateLoginResponse().loginResult, actual.data?.loginResult)
                Assert.assertNotNull(actual.data)
                Assert.assertTrue(actual is Condition.Success)
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun`when getRegister repo not Null and has value and return success`() = runTest {
            repo.setUserRegister("harvey","email","124567").test {
                awaitItem()
                val actual = awaitItem()
                    Assert.assertEquals(DataDummy.generateAddResponse(), actual.data)
                    Assert.assertNotNull(actual.data)
                    Assert.assertTrue(actual is Condition.Success)
                cancelAndConsumeRemainingEvents()
            }
    }

    @Test
    fun`when repository delete sesi and token return value`() = runTest {
        repo.deleteTokenandSesi().test {
            awaitItem()
            val actual = awaitItem()
            Assert.assertEquals(true, actual.data)
            Assert.assertNotNull(actual.data)
            Mockito.verify(pref).edit()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun`when setData repo has value and not null and return success`() = runTest {

        val file= mock(File::class.java)
        val description = "desc"
        val photo : File = file
        val requestImageFile = photo.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultiPart : MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            photo.name,
            requestImageFile
        )

            repo.setAddStory("harvey", description, photo).test {
                awaitItem()
                val actual = awaitItem()
                Assert.assertNotNull(actual.data)
                Assert.assertTrue(actual is Condition.Success)
                Assert.assertEquals(actual.data, DataDummy.generateAddResponse())
                cancelAndConsumeRemainingEvents()
            }
    }
}