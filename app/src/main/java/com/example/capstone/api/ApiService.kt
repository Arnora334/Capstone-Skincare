package com.example.capstone.api

import com.example.capstone.login.LoginRequest
import com.example.capstone.login.LoginResponse
import com.example.capstone.profile.ProfileResponse
import com.example.capstone.register.RegisterRequest
import com.example.capstone.result.ProcessImageRequest
import com.example.capstone.result.SkincareResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Response

interface ApiService {
    @POST("/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("/auth/signup")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): ResponseBody

    @GET("/auth/profile")
    suspend fun getUsers(
        @Header("x-access-token") token: String
    ): ProfileResponse

    @POST("/process-image")
    suspend fun processImage(
        @Body request: ProcessImageRequest
    ): Response<SkincareResponse>

}
