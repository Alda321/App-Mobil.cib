package com.example.loloytar.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {

    private const val BASE_URL = "https://modeloweb.azurewebsites.net/api/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API para platos
    val platoApi: PlatoApi by lazy {
        retrofit.create(PlatoApi::class.java)
    }

    // API para usuarios
    val usuarioApi: UsuarioApi by lazy {
        retrofit.create(UsuarioApi::class.java)
    }

    //API para Pedidos
    val pedidosApi: PedidoApi by lazy {
        retrofit.create(PedidoApi::class.java)
    }

    //API para Detalle de pedido
    val pedidosDetalleApi: PedidoDetalleApi by lazy {
        retrofit.create(PedidoDetalleApi::class.java)
    }
}
