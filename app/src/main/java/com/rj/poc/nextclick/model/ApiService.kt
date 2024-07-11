package com.rj.poc.nextclick.model

import com.rj.poc.nextclick.model.data.LoginRequestModel
import com.rj.poc.nextclick.model.data.LoginResponse
import com.rj.poc.nextclick.model.data.RegistrationRequestModel
import com.rj.poc.nextclick.model.data.RegistrationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun userLogin(@Body loginRequestModel: LoginRequestModel) : Response<LoginResponse>

    @POST("register")
    suspend fun userRegistration(@Body registrationRequestModel: RegistrationRequestModel) : Response<RegistrationResponse>
}