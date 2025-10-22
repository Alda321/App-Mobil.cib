package com.example.loloytar.activities

import adapter.PlatoAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loloytar.R
import com.example.loloytar.api.RetrofitInstance
import com.example.loloytar.model.Plato
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlatoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var platoAdapter: PlatoAdapter
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var bottomNav: BottomNavigationView

    private val listaPlatos: MutableList<Plato> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plato)

        recyclerView = findViewById(R.id.recyclerViewIndex)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fabAdd = findViewById(R.id.floatingActionButton)
        bottomNav = findViewById(R.id.bottom_navigation)

        // Inicializar Adapter una sola vez
        platoAdapter = PlatoAdapter(
            listaPlatos,
            onEditarClick = { plato ->
                val intent = Intent(this, CrearPlatoActivity::class.java)
                intent.putExtra("plato_id", plato.id)
                startActivity(intent)
            },
            onEliminarClick = { plato ->
                eliminarPlato(plato)
            }
        )
        recyclerView.adapter = platoAdapter

        // FloatingActionButton
        fabAdd.setOnClickListener {
            startActivity(Intent(this, CrearPlatoActivity::class.java))
        }

        // BottomNavigationView
        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_platos -> true // Ya estás aquí
                R.id.nav_pedidos -> {
                    startActivity(Intent(this, PedidoActivity::class.java))
                    true
                }
                R.id.nav_lista_general -> {
                    startActivity(Intent(this, Lista_GeneralActivity::class.java))
                    true
                }
                R.id.nav_usuarios -> {
                    startActivity(Intent(this, UsuarioActivity::class.java))
                    true
                }
                else -> false
            }
        }


        // Cargar platos al iniciar
        obtenerPlatos()
    }

    private fun obtenerPlatos() {
        val call: Call<List<Plato>> = RetrofitInstance.platoApi.getPlatos()
        call.enqueue(object : Callback<List<Plato>> {
            override fun onResponse(call: Call<List<Plato>>, response: Response<List<Plato>>) {
                if (response.isSuccessful) {
                    listaPlatos.clear()
                    listaPlatos.addAll(response.body() ?: emptyList())
                    platoAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@PlatoActivity, "Error del servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Plato>>, t: Throwable) {
                Toast.makeText(this@PlatoActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun eliminarPlato(plato: Plato) {
        val call = RetrofitInstance.platoApi.deletePlato(plato.id)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PlatoActivity, "${plato.nombre} eliminado", Toast.LENGTH_SHORT).show()
                    listaPlatos.remove(plato)
                    platoAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@PlatoActivity, "Error al eliminar: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@PlatoActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
