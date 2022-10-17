package com.example.storyapp.ui.feed

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.enums.RequestStatus
import com.example.storyapp.usecase.GetAllStoryUseCase
import com.example.storyapp.utils.AppPreference
import com.example.storyapp.utils.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getAllStoryUseCase: GetAllStoryUseCase,
    private val app: Application
): AndroidViewModel(app) {

    private val _loadingLiveData = MutableStateFlow(false)
    val loadingLiveData get() = _loadingLiveData.asSharedFlow()

    private val _allStoryLivedata = MutableStateFlow<List<StoryModel>?>(null)
    val allStoryLiveData get() = _allStoryLivedata.asSharedFlow()

    private val _isLogOut = MutableStateFlow(false)
    val isLogOut get() = _isLogOut.asSharedFlow()

    fun getAllStory(){
        viewModelScope.launch {
            getAllStoryUseCase().collect {
                when(it.requestStatus){
                    RequestStatus.LOADING -> {
                        _loadingLiveData.emit(true)
                    }
                    RequestStatus.SUCCESS -> {
                        _loadingLiveData.emit(false)
                        _allStoryLivedata.emit(it.data?.listStory)
                    }
                    RequestStatus.ERROR -> {
                        _loadingLiveData.emit(false)
                    }
                }
            }
        }
    }

    fun logOut(){
        viewModelScope.launch {
            val isLogin = AppPreference.getPreference(app)
                .getBoolean(AppPreference.IS_USER_LOGIN, false)
            val token = AppPreference.getPreference(app)
                .getString(AppPreference.USER_TOKEN, "")


            if (isLogin && token != ""){
                AppPreference.setPreference(app.applicationContext)
                    .putBoolean(AppPreference.IS_USER_LOGIN, false).apply()

                AppPreference.setPreference(app.applicationContext)
                    .putString(AppPreference.USER_TOKEN, "").apply()

                _isLogOut.emit(true)
            }
        }

    }
}