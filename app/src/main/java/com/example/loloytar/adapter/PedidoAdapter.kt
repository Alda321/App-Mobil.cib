package com.example.loloytar.adapters

import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loloytar.R
import com.example.loloytar.model.Pedido
import com.example.loloytar.model.Plato
import java.util.Locale

class PedidoAdapter(
    private var pedidos: List<Pedido>,
    private val listaPlatos: List<Plato>
) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

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

        // Formatear fecha
        val formatoApi = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault())
        val formatoMostrar = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fecha = try {
            formatoApi.parse(pedido.fecha)?.let { formatoMostrar.format(it) } ?: pedido.fecha
        } catch (e: Exception) {
            pedido.fecha
        }
        holder.txtFecha.text = "Fecha: $fecha"

        // Mostrar estado
        val estadoTexto = when (pedido.estado) {
            0 -> "Pendiente"
            1 -> "Preparando"
            2 -> "Listo"
            3 -> "Entregado"
            4 -> "Cancelado"
            else -> "Desconocido"
        }
        holder.txtEstado.text = "Estado: $estadoTexto"

        // Mostrar nombres de los platos
        holder.txtPlato.text = if (!pedido.detalles.isNullOrEmpty()) {
            "Platos: " + pedido.detalles.joinToString { detalle ->
                val platoNombre = listaPlatos.find { it.id == detalle.platoId }?.nombre ?: "Desconocido"
                "$platoNombre x${detalle.cantidad}"
            }
        } else {
            "Platos: -"
        }
    }

    override fun getItemCount(): Int = pedidos.size

    fun actualizarLista(nuevaLista: List<Pedido>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }
}
