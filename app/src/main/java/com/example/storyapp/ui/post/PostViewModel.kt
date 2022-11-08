package com.example.storyapp.ui.post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.ServiceSuccessResponse
import com.example.storyapp.enums.RequestStatus
import com.example.storyapp.usecase.PostNewStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postNewStoryUseCase: PostNewStoryUseCase
): ViewModel() {

    private val _postResult = MutableLiveData<ServiceSuccessResponse?>()
    val postResult get() = _postResult

    private val _errorResult = MutableLiveData<String>()
    val errorResult get() = _errorResult

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
                        _loadingResult.emit(true)
                    }
                    RequestStatus.SUCCESS -> {
                        _loadingResult.emit(false)
                        _postResult.value = it.data
                    }
                    RequestStatus.ERROR -> {
                        _loadingResult.emit(false)
                        _errorResult.value = it.message.toString()
                    }
                }
            }
        }
    }

}