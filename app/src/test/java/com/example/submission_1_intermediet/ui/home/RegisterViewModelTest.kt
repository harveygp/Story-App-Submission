package com.example.submission_1_intermediet.ui.home

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.repository.UserRepositoryImpl
import com.example.submission_1_intermediet.ui.register.RegisterViewModel
import com.example.submission_1_intermediet.utils.Condition
import com.example.submission_1_intermediet.utils.FakeDataSource
import com.example.submission_1_intermediet.utils.FakeRepository
import com.example.submission_1_intermediet.utils.MainDispatcherRule
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
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var pref: SharedPreferences

    @Mock
    private lateinit var realRepo : UserRepositoryImpl

    @Test
    fun`testing viewModel Register has value and not null and return success`() = runTest{
        val viewModel = RegisterViewModel(realRepo, pref)

        val expected = flow{emit(Condition.Success(data = ApiResponse(error = false, message = "berhasil")))}

        Mockito.`when`(realRepo.setUserRegister("harvey","harvey.narukami@gmail.com","1234567")).thenReturn(expected)

        val real = viewModel.setUserRegister("harvey","harvey.narukami@gmail.com","1234567")

        Mockito.verify(realRepo).setUserRegister("harvey","harvey.narukami@gmail.com","1234567")
        Assert.assertTrue(real is Condition.Success)
        Assert.assertEquals(false,real?.data?.error)
        Assert.assertNotNull(real?.data)
    }

    @Test
    fun`testing viewModel Register called repo return error`() = runTest{
        val viewModel = RegisterViewModel(realRepo, pref)

        val expected = flow{emit(Condition.Error("Error", data = ApiResponse(error = true, message = "error")))}

        Mockito.`when`(realRepo.setUserRegister("harvey","harvey.narukami@gmail.com","1234567")).thenReturn(expected)

        val real = viewModel.setUserRegister("harvey","harvey.narukami@gmail.com","1234567")

        Mockito.verify(realRepo).setUserRegister("harvey","harvey.narukami@gmail.com","1234567")
        Assert.assertTrue(real is Condition.Error)
        Assert.assertEquals(true,real?.data?.error)
        Assert.assertNotNull(real?.data)
    }

}