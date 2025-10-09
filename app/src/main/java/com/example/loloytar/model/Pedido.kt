package com.example.loloytar.model

data class Pedido(
    val id: Int = 0,
    val numeroMesa: String = "",
    val fecha: String = "",
    val estado: Int = 0,
    val pagado: Boolean = false,
    val comentario: String = "",
    val mozoId: Int = 0,
    val cantidad: Int = 1,
    val plato: Plato? = null,
    val detalles: List<PedidoDetalle> = emptyList()

)

