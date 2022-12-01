package com.example.submission_1_intermediet.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.remote.response.LoginResponse
import com.example.submission_1_intermediet.domain.repository.UserRepository
import com.example.submission_1_intermediet.utils.Condition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> = _response

    private val _data = MutableLiveData<Condition<LoginResponse>>()
    val data: LiveData<Condition<LoginResponse>> = _data

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    init {
        _isLoading.postValue(false)
    }

    fun setUserLogin(email: String?, password: String): Condition<LoginResponse>? {
        _isLoading.value = true
        var temp : Condition<LoginResponse>? = null
        viewModelScope.launch {
            repository.setUserLogin(email, password).onEach { condition ->
                when (condition) {
                    is Condition.Loading -> _isLoading.postValue(true)
                    is Condition.Success -> {
                        _response.value = condition.data?.message!!
                        _data.value = condition
                        temp = condition
                        _isLoading.postValue(false)
                        try {
                            prefs.edit()
                                .putString("Token", condition.data.loginResult?.token)
                                .putString("Sesi", "Masuk")
                                .apply()
                        } catch (e: NullPointerException) {
                            _token.value = "kosong"
                        }
                    }
                    is Condition.Error -> {
                        _response.postValue("error")
                        _data.value = condition
                        temp = condition
                        _isLoading.postValue(false)
                    }
                }
            }.launchIn(viewModelScope)
        }
        return temp
    }
}