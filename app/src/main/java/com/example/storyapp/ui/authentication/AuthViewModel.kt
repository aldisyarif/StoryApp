package com.example.storyapp.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.LoginBody
import com.example.storyapp.data.model.RegisterBody
import com.example.storyapp.data.model.ServiceSuccessResponse
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.enums.RequestStatus
import com.example.storyapp.usecase.PostLoginUserUseCase
import com.example.storyapp.usecase.PostRegisterUseCase
import com.example.storyapp.utils.AppPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val postLoginUserUseCase: PostLoginUserUseCase,
    private val postRegisterUseCase: PostRegisterUseCase,
    private val app: Application
): AndroidViewModel(app) {

    private var _loadingState = MutableStateFlow(false)
    val loadingState get() = _loadingState.asSharedFlow()

    private var _errorResult = MutableSharedFlow<String>()
    val errorResult get() = _errorResult.asSharedFlow()

    private var _resultUser = MutableSharedFlow<UserModel?>()
    val resultUser get() = _resultUser.asSharedFlow()

    private var _loadingRegisterState = MutableStateFlow(false)
    val loadingRegisterState get() = _loadingRegisterState.asSharedFlow()

    private var _errorRegisterResult = MutableSharedFlow<String>()
    val errorRegisterResult get() = _errorRegisterResult.asSharedFlow()

    private var _resultRegisterUser = MutableSharedFlow<ServiceSuccessResponse?>()
    val resultRegisterUser get() = _resultRegisterUser.asSharedFlow()

    fun loginUser(email: String, password: String){
        viewModelScope.launch {
            val loginBody = LoginBody(email, password)
            postLoginUserUseCase(loginBody).collect {
                when(it.requestStatus){
                    RequestStatus.LOADING -> {
                        _loadingState.emit(true)
                    }
                    RequestStatus.SUCCESS -> {
                        _loadingState.emit(false)
                        it.data?.loginResult?.let { it1 ->
                            saveUserPreference(it1)
                            _resultUser.emit(it1)
                        }
                    }
                    RequestStatus.ERROR -> {
                        _loadingState.emit(false)
                        it.message.let { it1 ->
                            it1?.let { it2 ->
                                _errorResult.emit(it2)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveUserPreference(user: UserModel?) {
        if (user == null) return
        AppPreference.setPreference(app.applicationContext)
            .putBoolean(AppPreference.IS_USER_LOGIN, true).apply()

        AppPreference.setPreference(app.applicationContext)
            .putString(AppPreference.USER_TOKEN, user.token).apply()
    }

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            val registerBody = RegisterBody(name, email, password)
            postRegisterUseCase(registerBody).collect {
                when(it.requestStatus){
                    RequestStatus.LOADING -> {
                        _loadingRegisterState.emit(true)
                    }
                    RequestStatus.SUCCESS -> {
                        _loadingRegisterState.emit(false)
                        _resultRegisterUser.emit(it.data)
                    }
                    RequestStatus.ERROR -> {
                        _loadingRegisterState.emit(false)
                        it.data?.message?.let { it1 ->
                            _errorRegisterResult.emit(it1)
                        }
                    }
                }
            }
        }
    }
}