package com.example.loloytar.api

import api.PlatoApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: PlatoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://proyecto-loloyta-f9akefdbh7fxf4ac.eastus-01.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlatoApi::class.java)
    }
}
