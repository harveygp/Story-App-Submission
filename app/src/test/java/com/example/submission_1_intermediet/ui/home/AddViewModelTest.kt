package com.example.submission_1_intermediet.ui.home

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.repository.UserRepositoryImpl
import com.example.submission_1_intermediet.ui.add.AddViewModel
import com.example.submission_1_intermediet.utils.Condition
import com.example.submission_1_intermediet.utils.FakeDataSource
import com.example.submission_1_intermediet.utils.FakeRepository
import com.example.submission_1_intermediet.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AddViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var pref: SharedPreferences

    @Mock
    private lateinit var repository : UserRepositoryImpl


    @Test
    fun`testing viewModel Add has value and Not Null and return success`() = runTest{
        val viewModel = AddViewModel(repository, pref)

        val file= mock(File::class.java)
        val description = "desc"
        val photo : File = file

        val expected = flow{emit(Condition.Success(ApiResponse(error = false, message = "berhasil")))}

        `when`(repository.setAddStory("1234567",description = "harvey", photo = photo)).thenReturn(expected)

        val real = viewModel.setAddStory("1234567",description = "harvey", photo = photo)

        Mockito.verify(repository).setAddStory("1234567",description = "harvey", photo = photo)
        Assert.assertTrue(real is Condition.Success)
        Assert.assertEquals(false,real?.data?.error)
        Assert.assertNotNull(real?.data)
    }

    @Test
    fun`testing viewModel Add called repo return Error`() = runTest{
        val viewModel = AddViewModel(repository, pref)

        val file= Mockito.mock(File::class.java)
        val description = "desc"
        val photo : File = file

        val expected = flow{emit(Condition.Error("Error", data = ApiResponse(error = true, message = "error")))}

        `when`(repository.setAddStory("1234567",description = "harvey", photo = photo)).thenReturn(expected)

        val real = viewModel.setAddStory("1234567",description = "harvey", photo = photo)

        Mockito.verify(repository).setAddStory("1234567",description = "harvey", photo = photo)
        Assert.assertTrue(real is Condition.Error)
        Assert.assertEquals(true,real?.data?.error)
        Assert.assertNotNull(real?.data)
    }
}