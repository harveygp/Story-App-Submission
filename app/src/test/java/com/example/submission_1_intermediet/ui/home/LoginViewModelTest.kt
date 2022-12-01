package com.example.submission_1_intermediet.ui.home

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.remote.response.LoginResponse
import com.example.submission_1_intermediet.data.remote.response.LoginResult
import com.example.submission_1_intermediet.data.repository.UserRepositoryImpl
import com.example.submission_1_intermediet.ui.add.AddViewModel
import com.example.submission_1_intermediet.ui.login.LoginViewModel
import com.example.submission_1_intermediet.utils.Condition
import com.example.submission_1_intermediet.utils.FakeDataSource
import com.example.submission_1_intermediet.utils.FakeRepository
import com.example.submission_1_intermediet.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
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
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var pref: SharedPreferences

    @Mock
    private lateinit var realRepo : UserRepositoryImpl

    @Test
    fun`testing viewModel login has value and not null and return success`() = runTest{
        val viewModel = LoginViewModel(realRepo, pref)

        val expected = flow{emit(Condition.Success(
                                LoginResponse(
                                    loginResult = LoginResult(name = "Harvey", userId = "1234", token = "1234jsad"),
                                    error = false,
                                    message = "berhasil"))
                                )
                            }

        Mockito.`when`(realRepo.setUserLogin("harvey.narukami@gmail.com","1234567")).thenReturn(expected)

        val real = viewModel.setUserLogin("harvey.narukami@gmail.com","1234567")

        Mockito.verify(realRepo).setUserLogin("harvey.narukami@gmail.com","1234567")
        Assert.assertTrue(real is Condition.Success)
        Assert.assertEquals("berhasil",real?.data?.message )
        Assert.assertNotNull(real?.data)
    }

    @Test
    fun`testing viewModel Login called repo return Error`() = runTest{
        val viewModel = LoginViewModel(realRepo, pref)

        val expected = flow{emit(Condition.Error("error",
            LoginResponse(
                loginResult = null,
                error = true,
                message = "error"))
            )
        }

        Mockito.`when`(realRepo.setUserLogin("harvey.narukami@gmail.com","1234567")).thenReturn(expected)

        val real = viewModel.setUserLogin("harvey.narukami@gmail.com","1234567")

        Mockito.verify(realRepo).setUserLogin("harvey.narukami@gmail.com","1234567")
        Assert.assertTrue(real is Condition.Error)
        Assert.assertEquals("error",real?.data?.message  )
        Assert.assertNotNull(real?.data )
    }
}