package com.example.loloytar.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loloytar.R
import com.example.loloytar.model.Pedido

class PedidoAdapter(private var pedidos: List<Pedido>) :
    RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNumeroMesa: TextView = itemView.findViewById(R.id.txtNumeroMesa)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
        val txtComentario: TextView = itemView.findViewById(R.id.txtComentario)
        val txtPagado: TextView = itemView.findViewById(R.id.txtPagado)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtPlato: TextView = itemView.findViewById(R.id.txtPlato)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]

        holder.txtNumeroMesa.text = "Mesa: ${pedido.numeroMesa}"
        holder.txtComentario.text = "Comentario: ${pedido.comentario}"
        holder.txtPagado.text = "Pagado: ${if (pedido.pagado) "Sí" else "No"}"
        holder.txtFecha.text = "Fecha: ${pedido.fecha.take(16)}"

        // 👇 Si tienes lista de detalles (platos)
        if (pedido.detalles.isNotEmpty()) {
            val platosTexto = pedido.detalles.joinToString("\n") {
                val nombrePlato = it.plato?.nombre ?: "Plato #${it.platoId}"
                "${nombrePlato} (x${it.cantidad})"
            }
            holder.txtPlato.text = platosTexto
    } else {
            holder.txtPlato.text = "Sin platos"
        }

        val estadoTexto = when (pedido.estado) {
            0 -> "Pendiente"
            1 -> "Preparando"
            2 -> "Listo"
            3 -> "Entregado"
            4 -> "Cancelado"
            else -> "Desconocido"
        }

        holder.txtEstado.text = "Estado: $estadoTexto"
    }


    override fun getItemCount(): Int = pedidos.size

    fun actualizarLista(nuevaLista: List<Pedido>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }
}
