package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loloytar.R
import com.example.loloytar.model.Plato

class PlatoAdapter(private val lista: List<Plato>) :
    RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder>() {

    class PlatoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.tvNombre)
        val precio: TextView = itemView.findViewById(R.id.tvPrecio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_plato, parent, false)
        return PlatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlatoViewHolder, position: Int) {
        val plato = lista[position]
        holder.nombre.text = plato.nombre
        holder.precio.text = "S/. ${plato.precio}"
    }

    override fun getItemCount() = lista.size
}
