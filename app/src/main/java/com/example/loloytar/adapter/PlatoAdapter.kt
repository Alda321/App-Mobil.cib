package adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.loloytar.R
import com.example.loloytar.api.RetrofitInstance
import com.example.loloytar.model.Plato
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlatoAdapter(
    val lista: List<Plato>,
    private val onEditarClick: (Plato) -> Unit,
    private val onEliminarClick: (Plato) -> Unit
) : RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder>() {

    class PlatoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        val precio: TextView = itemView.findViewById(R.id.tvPrecio)
        val estadoView: View = itemView.findViewById(R.id.viewEstado)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
        val btnEstado: Button = itemView.findViewById(R.id.btnEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plato, parent, false)
        return PlatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlatoViewHolder, position: Int) {
        val plato = lista[position]

        // Datos
        holder.nombre.text = plato.nombre
        holder.precio.text = "S/. ${plato.precio}"
        actualizarEstadoVisual(holder, plato)

        // Botones
        holder.btnEditar.setOnClickListener { onEditarClick(plato) }
        holder.btnEliminar.setOnClickListener { onEliminarClick(plato) }

        holder.btnEstado.setOnClickListener {
            // Cambiar estado local
            val nuevoEstado = !plato.activo
            val call: Call<Void> = if (nuevoEstado) {
                RetrofitInstance.platoApi.activarPlato(plato.id)
            } else {
                RetrofitInstance.platoApi.desactivarPlato(plato.id)
            }

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        plato.activo = nuevoEstado
                        actualizarEstadoVisual(holder, plato)
                        Toast.makeText(holder.itemView.context, "Estado actualizado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(holder.itemView.context, "Error al actualizar: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(holder.itemView.context, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun actualizarEstadoVisual(holder: PlatoViewHolder, plato: Plato) {
        val colorEstado = if (plato.activo)
            ContextCompat.getColor(holder.itemView.context, R.color.teal_200)
        else
            ContextCompat.getColor(holder.itemView.context, R.color.purple_500)

        holder.estadoView.setBackgroundColor(colorEstado)
        holder.btnEstado.text = if (plato.activo) "Activo" else "Inactivo"
    }

    override fun getItemCount() = lista.size
}
