package com.rj.poc.nextclick.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rj.poc.nextclick.model.Repository
import com.rj.poc.nextclick.model.data.LoginRequestModel
import com.rj.poc.nextclick.model.data.LoginResponse
import retrofit2.Response
import androidx.lifecycle.viewModelScope
import com.rj.poc.nextclick.model.data.RegistrationRequestModel
import com.rj.poc.nextclick.model.data.RegistrationResponse
import com.rj.poc.nextclick.viewmodel.utils.ApiResponse
import kotlinx.coroutines.launch

class LoginSignUpViewModel (private val repository: Repository) : ViewModel() {

    private val _loginResponse = MutableLiveData<ApiResponse<Response<LoginResponse>>>()
    val loginResponse: LiveData<ApiResponse<Response<LoginResponse>>> get() = _loginResponse

    private val _registrationResponse = MutableLiveData<ApiResponse<Response<RegistrationResponse>>>()
    val registrationResponse: LiveData<ApiResponse<Response<RegistrationResponse>>> get() = _registrationResponse

    fun userLogin(loginReq: LoginRequestModel) {
        viewModelScope.launch {
            _loginResponse.postValue(ApiResponse.Loading)
            runCatching {
                repository.userLogin(loginReq)
            }.onSuccess { res ->
                _loginResponse.postValue(ApiResponse.Success(res))
            }.onFailure { throwable ->
                _loginResponse.postValue(ApiResponse.Error(throwable))
            }
        }
    }
    fun userRegistration(registationReq: RegistrationRequestModel) {
        viewModelScope.launch {
            _registrationResponse.postValue(ApiResponse.Loading)
            runCatching {
                repository.userRegistration(registationReq)
            }.onSuccess { res ->
                _registrationResponse.postValue(ApiResponse.Success(res))
            }.onFailure { throwable ->
                _registrationResponse.postValue(ApiResponse.Error(throwable))
            }
        }
    }
}