package com.example.submission_1_intermediet.ui.map

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submission_1_intermediet.data.remote.response.ApiResponse
import com.example.submission_1_intermediet.data.remote.response.ListStoryItem
import com.example.submission_1_intermediet.data.remote.response.StoriesResponse
import com.example.submission_1_intermediet.domain.repository.UserRepository
import com.example.submission_1_intermediet.utils.Condition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel@Inject constructor(
    private val repository : UserRepository,
    private val prefs : SharedPreferences
) : ViewModel()  {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories : LiveData<List<ListStoryItem>> = _listStories

    private val _data = MutableLiveData<Condition<StoriesResponse>>()
    val data: LiveData<Condition<StoriesResponse>> = _data

    private val _pref : String? = prefs.getString("Token", null) ?: "kosong"
    val pref = _pref

    init {
        _isLoading.postValue(true)
    }

    fun getStoriesLocation(token : String? = _pref ?: "kosong"): Condition<StoriesResponse>? {
        _isLoading.postValue(true)
        var temp : Condition<StoriesResponse>? = null
        viewModelScope.launch{
            repository.getStoriesLocation(token).onEach { condition ->
                when(condition){
                    is Condition.Loading -> _isLoading.postValue(true)
                    is Condition.Success -> {
                        _listStories.postValue(condition.data?.listStory)
                        _data.value = condition
                        temp = condition
                        _isLoading.postValue(false)
                    }
                    is Condition.Error -> {
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