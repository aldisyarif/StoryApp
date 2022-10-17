package com.example.storyapp.ui.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.storyapp.data.model.ServiceSuccessResponse
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.enums.RequestStatus
import com.example.storyapp.usecase.PostNewStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postNewStoryUseCase: PostNewStoryUseCase
): ViewModel() {

    private val _postResult = MutableSharedFlow<ServiceSuccessResponse?>()
    val postResult get() = _postResult.asSharedFlow()

    private val _errorResult = MutableSharedFlow<String>()
    val errorResult get() = _errorResult.asSharedFlow()

    private val _loadingResult = MutableStateFlow(false)
    val loadingResult get() = _loadingResult.asSharedFlow()

    fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ){
        viewModelScope.launch {
            postNewStoryUseCase(file, description, lat, lon).collect {
                when(it.requestStatus){
                    RequestStatus.LOADING -> {
                        Log.d("PostVieModel", "addNewStory:  LOADING")
                        _loadingResult.emit(true)
                    }
                    RequestStatus.SUCCESS -> {
                        Log.d("PostVieModel", "addNewStory: SUCCESS")
                        _loadingResult.emit(false)
                        _postResult.emit(it.data)
                    }
                    RequestStatus.ERROR -> {
                        Log.d("PostVieModel", "addNewStory: ERROR")
                        _loadingResult.emit(false)
                        _errorResult.emit(it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

}