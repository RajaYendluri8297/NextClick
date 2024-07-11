package com.rj.poc.nextclick.model

import com.rj.poc.nextclick.model.data.LoginRequestModel
import com.rj.poc.nextclick.model.data.LoginResponse
import com.rj.poc.nextclick.model.data.RegistrationRequestModel
import com.rj.poc.nextclick.model.data.RegistrationResponse
import retrofit2.Response

class Repository {

    private val apiService = RetrofitService.apiService

    suspend fun userLogin(loginReq : LoginRequestModel) : Response<LoginResponse> {
        return apiService.userLogin(loginReq)
    }
    suspend fun userRegistration(registationReq : RegistrationRequestModel) : Response<RegistrationResponse> {
        return apiService.userRegistration(registationReq)
    }

}