package com.example.loloytar.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import adapter.PlatoAdapter
import com.example.loloytar.R
import com.example.loloytar.api.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Index : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var platoAdapter: PlatoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)

        recyclerView = findViewById(R.id.recyclerViewPlatos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        obtenerPlatos()
    }

    private fun obtenerPlatos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val listaPlatos = RetrofitInstance.api.getPlatos()
                runOnUiThread {
                    platoAdapter = PlatoAdapter(listaPlatos)
                    recyclerView.adapter = platoAdapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
