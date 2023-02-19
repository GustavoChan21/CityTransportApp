package com.example.citytransport.network

import com.example.citytransport.request.LoginRequest
import com.example.citytransport.request.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import android.telecom.Call as Call1

interface ApiInterface {
    @POST("login.php")
    fun postLogin(@Body validateRequest:LoginRequest?): Call<LoginResponse>





}