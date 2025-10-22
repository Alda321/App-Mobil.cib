package com.example.loloytar.model
data class Pedido(
    val id: Int? = null,
    val numeroMesa: String,
    val fecha: String,
    val estado: Int,
    val pagado: Boolean,
    val comentario: String,
    val mozoId: Int,
    val detalles: List<PedidoDetalle> = emptyList() // <-- así nunca será null
)
