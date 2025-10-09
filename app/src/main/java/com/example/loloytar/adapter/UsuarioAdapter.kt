package com.example.loloytar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loloytar.R
import com.example.loloytar.model.Usuario

class UsuarioAdapter(
    private var listaUsuarios: List<Usuario>
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    // Clase interna que representa cada fila del RecyclerView
    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreUsuario)
        val txtLogin: TextView = itemView.findViewById(R.id.txtLoginUsuario)
        val txtRol: TextView = itemView.findViewById(R.id.txtRolUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        holder.txtNombre.text = usuario.nombre
        holder.txtLogin.text = usuario.login
        holder.txtRol.text = usuario.rol
    }

    override fun getItemCount(): Int = listaUsuarios.size

    // Método opcional para actualizar lista
    fun actualizarLista(nuevaLista: List<Usuario>) {
        listaUsuarios = nuevaLista
        notifyDataSetChanged()
    }
}
