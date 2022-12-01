package com.example.submission_1_intermediet.ui.home

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.remote.response.StoriesResponse
import com.example.submission_1_intermediet.data.repository.UserRepositoryImpl
import com.example.submission_1_intermediet.ui.map.MapsViewModel
import com.example.submission_1_intermediet.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var pref: SharedPreferences

    @Mock
    private lateinit var realRepo : UserRepositoryImpl


    @Test
    fun`testing viewModel getStoriesLocation has value and not Null and return success`() = runTest{
        val viewModel = MapsViewModel(realRepo, pref)

        val expected = flow{emit(Condition.Success(data = StoriesResponse(error = true, message = "error", listStory = DataDummy.generateDummyListStory())))}

        Mockito.`when`(realRepo.getStoriesLocation("1234567")).thenReturn(expected)

        val real = viewModel.getStoriesLocation("1234567")

        Mockito.verify(realRepo).getStoriesLocation("1234567")
        Assert.assertTrue(real is Condition.Success)
        Assert.assertEquals(real?.data?.listStory, DataDummy.generateDummyStory().listStory)
        Assert.assertNotNull(real?.data)
    }

    @Test
    fun`testing viewModel getStoriesLocation called repo and return error`() = runTest{
        val viewModel = MapsViewModel(realRepo, pref)

        val expected = flow{emit(Condition.Error("Error", data = StoriesResponse(error = true, message = "error", listStory = emptyList())))}

        Mockito.`when`(realRepo.getStoriesLocation("1234567")).thenReturn(expected)

        val real = viewModel.getStoriesLocation("1234567")

        Mockito.verify(realRepo).getStoriesLocation("1234567")
        Assert.assertEquals(real?.message, "Error" )
        Assert.assertNotNull(real?.data)
        Assert.assertTrue(real is Condition.Error)

    }
}