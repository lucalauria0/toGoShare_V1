package com.example.carsharingv2.controllers


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIService {
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)
    }
}
