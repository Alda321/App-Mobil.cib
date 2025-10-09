package com.example.loloytar.model


data class PedidoDetalle(
    val id: Int = 0,
    val platoId: Int = 0,
    val cantidad: Int = 0,
    val plato: Plato? = null
)

