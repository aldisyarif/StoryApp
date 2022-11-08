package com.example.storyapp.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.ServiceSuccessResponse
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.enums.RequestStatus
import com.example.storyapp.usecase.GetAllMapStoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllMapStoryUseCase: GetAllMapStoryUseCase
)  : ViewModel() {

    private val _loadingLiveData = MutableStateFlow(false)
    val loadingLiveData get() = _loadingLiveData.asSharedFlow()

    private val _allStoryLivedata = MutableLiveData<List<StoryModel>?>(null)
    val allStoryLiveData get() = _allStoryLivedata

    private val _errorResult = MutableLiveData<String>(null)
    val errorResult get() = _errorResult


    fun getAllMapStory(location: Int){
        viewModelScope.launch {
            getAllMapStoryUseCase(location).collect {
                when(it.requestStatus){
                    RequestStatus.LOADING -> {
                        _loadingLiveData.emit(true)
                    }
                    RequestStatus.SUCCESS -> {
                        _loadingLiveData.emit(false)
                        _allStoryLivedata.value = it.data?.listStory
                    }
                    RequestStatus.ERROR -> {
                        _loadingLiveData.emit(false)
                        it.message?.let { message ->
                            _errorResult.value = message
                        }
                    }
                }
            }
        }
    }

}