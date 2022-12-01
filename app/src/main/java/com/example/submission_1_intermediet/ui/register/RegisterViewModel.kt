package com.example.submission_1_intermediet.ui.register

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.domain.repository.UserRepository
import com.example.submission_1_intermediet.utils.Condition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel@Inject constructor(
    private val repository : UserRepository,
    private val prefs : SharedPreferences
) : ViewModel()  {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _data = MutableLiveData<Condition<ApiResponse>>()
    val data: LiveData<Condition<ApiResponse>> = _data

    private val _responseRegister = MutableLiveData<Boolean>()
    val responseRegister: LiveData<Boolean> = _responseRegister

    init {
        _isLoading.postValue(false)
    }

    fun setUserRegister(name : String?, email : String, password : String): Condition<ApiResponse>? {
        _isLoading.value = true
        var temp : Condition<ApiResponse>? = null
        viewModelScope.launch {
            repository.setUserRegister(name,email, password).onEach { condition ->
                when(condition){
                    is Condition.Loading -> _isLoading.postValue(true)
                    is Condition.Success -> {
                        _responseRegister.value = condition.data?.error!!
                        _data.value = condition
                        temp = condition
                        _isLoading.postValue(false)
                    }
                    is Condition.Error -> {
                        _responseRegister.postValue(true)
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