package com.example.loloytar.api

import com.example.loloytar.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface UsuarioApi {

    @GET("Usuario")
    fun getUsuarios(): Call<List<Usuario>>

    @GET("Usuario/{id}")
    fun getUsuario(@Path("id") id: Int): Call<Usuario>

    @POST("Usuario")
    fun postUsuario(@Body usuario: Map<String, String>): Call<Usuario>

    @PUT("Usuario/{id}")
    fun putUsuario(@Path("id") id: Int, @Body usuario: Usuario): Call<Usuario>

    @DELETE("Usuario/{id}")
    fun deleteUsuario(@Path("id") id: Int): Call<Void>
}
