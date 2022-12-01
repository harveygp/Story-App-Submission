package com.example.submission_1_intermediet.ui.add

import android.content.SharedPreferences
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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddViewModel@Inject constructor(
    private val repository : UserRepository,
    private val prefs : SharedPreferences
) : ViewModel()  {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<Boolean>()
    val response: LiveData<Boolean> = _response

    private val _data = MutableLiveData<Condition<ApiResponse>>()
    val data: LiveData<Condition<ApiResponse>> = _data

    private val _pref = prefs.getString("Token", null) ?: "kosong"
    var pref : String? = _pref

    init {
        _isLoading.postValue(false)
    }

    fun setAddStory(token : String = _pref ?: "kosong",description : String,photo : File): Condition<ApiResponse>? {
        _isLoading.postValue(true)
        var temp : Condition<ApiResponse>? = null
        viewModelScope.launch {
                repository.setAddStory(token, description,photo).onEach { condition ->
                    when(condition){
                        is Condition.Loading -> _isLoading.postValue(true)
                        is Condition.Success -> {
                            _response.value = condition.data?.error!!
                            _data.value = condition
                            temp = condition
                            _isLoading.postValue(false)
                        }
                        is Condition.Error -> {
                            _response.postValue(true)
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