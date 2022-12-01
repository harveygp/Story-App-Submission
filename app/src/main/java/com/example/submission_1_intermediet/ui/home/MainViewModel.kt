package com.example.submission_1_intermediet.ui.home

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.submission_1_intermediet.data.paging.StoriesPagingSource
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem
import com.example.submission_1_intermediet.data.remote.retrofit.ApiService
import com.example.submission_1_intermediet.domain.repository.UserRepository
import com.example.submission_1_intermediet.utils.Condition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val repository : UserRepository,
    private val prefs : SharedPreferences
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _pref = prefs.getString("Token", null)
    var pref = _pref

    init {
        _isLoading.postValue(true)
    }

    suspend fun getData() = repository.getStories().cachedIn(viewModelScope)

    fun setDeleteSesiandToken() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            repository.deleteTokenandSesi().onEach { condition ->
                when(condition) {
                    is Condition.Error -> {
                        pref = "kosong"
                        _isLoading.postValue(false)
                    }
                    is Condition.Success -> {
                        _isLoading.postValue(false)
                    }
                    is Condition.Loading -> _isLoading.postValue(true)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun changeLoadingState(state : Boolean) = _isLoading.postValue(state)

}