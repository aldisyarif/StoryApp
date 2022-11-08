package com.example.storyapp.ui.feed

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.model.StoryModel
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
): ViewModel() {

    private val _loadingLiveData = MutableStateFlow(false)
    val loadingLiveData get() = _loadingLiveData.asSharedFlow()

    private val _allStoryLivedata = MutableLiveData<Resources<PagingData<StoryModel>>?>()
    val allStoryLiveData get() = _allStoryLivedata

    private val _isLogOut = MutableStateFlow(false)
    val isLogOut get() = _isLogOut.asSharedFlow()

    fun getAllStory(){
        viewModelScope.launch {
            _loadingLiveData.emit(true)
            getAllStoryUseCase()
                .catch {
                    _loadingLiveData.emit(false)
                    _allStoryLivedata.value = it.localizedMessage?.let { it1 -> Resources.error(it1, null) }
                }.cachedIn(this)
                .collect {
                    _loadingLiveData.emit(false)
                    _allStoryLivedata.value = Resources.success(it)
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