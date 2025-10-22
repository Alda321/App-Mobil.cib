package com.example.loloytar.api

import com.example.loloytar.model.Pedido
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PedidoApi {
    @GET("Pedido")
    fun getPedidos(): Call<List<Pedido>>

    @GET("Pedido/{id}")
    fun getPedido(@Path("id") id: Int): Call<Pedido>

    @POST("Pedidos/Create")
    fun crearPedido(@Body pedido: Pedido): Call<Void>

    @POST("Pedido")
    fun postPedido(@Body pedido: Pedido): Call<Pedido>

    @PUT("Pedido/{id}")
    fun putPedido(@Path("id") id: Int, @Body pedido: Pedido): Call<Pedido>

    @DELETE("Pedido/{id}")
    fun deletePedido(@Path("id") id: Int): Call<Void>
}