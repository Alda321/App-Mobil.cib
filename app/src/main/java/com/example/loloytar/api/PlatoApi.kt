package com.example.loloytar.api

import com.example.loloytar.model.Plato
import retrofit2.Call
import retrofit2.http.*

interface PlatoApi {

    @GET("Plato")
    fun getPlatos(): Call<List<Plato>>

    @GET("Plato/{id}")
    fun getPlato(@Path("id") id: Int): Call<Plato>

    @POST("Plato")
    fun postPlato(@Body plato: Plato): Call<Plato>

    @PUT("Plato/{id}")
    fun putPlato(@Path("id") id: Int, @Body plato: Plato): Call<Plato>

    @DELETE("Plato/{id}")
    fun deletePlato(@Path("id") id: Int): Call<Void>

    @PATCH("Plato/activar/{id}")
    fun activarPlato(@Path("id") id: Int): Call<Void>

    @PATCH("Plato/desactivar/{id}")
    fun desactivarPlato(@Path("id") id: Int): Call<Void>

}
