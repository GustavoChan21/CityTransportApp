package com.example.citytransport

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.citytransport.network.ApiClient
import com.example.citytransport.network.ApiInterface
import com.example.citytransport.request.LoginRequest
import com.example.citytransport.request.LoginResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {

    lateinit var _user:TextInputEditText
    lateinit var _password:TextInputEditText
    lateinit var _aceptar:Button
    lateinit var request:ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        request = ApiClient.buildService(ApiInterface::class.java)

        _user = findViewById(R.id.login_user)
        _password = findViewById(R.id.login_password_editText)
        _aceptar = findViewById(R.id.aceptar)

        _aceptar.setOnClickListener{
            login(_user.text.toString(), _password.text.toString())
        }

    }
    fun login(user:String, password:String){
        //Toast.makeText(this, "${user} - ${password}", Toast.LENGTH_SHORT).show()
        val login = LoginRequest(user, password)
        val call = request.postLogin(login)
        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.body()!!.success){
                    val a = Intent(this@Login, MainActivityResultados:: class.java)
                    startActivity(a)
                    finish()
                }
                else{
                    Toast.makeText(this@Login, response.body()!!.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@Login, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}