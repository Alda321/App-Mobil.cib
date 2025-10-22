package com.example.loloytar.api

import com.example.loloytar.model.PedidoDetalle
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PedidoDetalleApi {
    @GET("PedidoDetalles")
    fun getPedidoDetalles(): Call<List<PedidoDetalle>>

    @POST("PedidoDetalles")
    fun postPedidoDetalle(@Body detalle: PedidoDetalle): Call<PedidoDetalle>
}