package com.example.citytransport.request

import com.example.citytransport.data.Result

//Envia los Datos
data class LoginRequest (
    val user:String,
    val  password:String

)

//Recibe los Datos/Respuesta
data class LoginResponse(
    val success: Boolean,
    val msg: String
)

