package com.example.loloytar.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loloytar.R
import com.example.loloytar.adapters.PedidoAdapter
import com.example.loloytar.api.RetrofitInstance
import com.example.loloytar.model.Pedido
import com.example.loloytar.model.PedidoDetalle
import com.example.loloytar.model.Plato
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PedidoActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: PedidoAdapter
    private lateinit var edtNumeroMesa: EditText
    private lateinit var edtComentario: EditText
    private lateinit var edtCantidad: EditText
    private lateinit var chkPagado: CheckBox
    private lateinit var spnEstado: Spinner
    private lateinit var spnPlato: Spinner
    private lateinit var btnGuardar: Button

    private var listaPlatos: List<Plato> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)

        recycler = findViewById(R.id.recyclerPedidos)
        edtNumeroMesa = findViewById(R.id.edtNumeroMesa)
        edtComentario = findViewById(R.id.edtComentario)
        edtCantidad = findViewById(R.id.edtCantidad)
        chkPagado = findViewById(R.id.chkPagado)
        spnEstado = findViewById(R.id.spnEstado)
        spnPlato = findViewById(R.id.spnPlato)
        btnGuardar = findViewById(R.id.btnGuardarPedido)

        recycler.layoutManager = LinearLayoutManager(this)

        btnGuardar.setOnClickListener { guardarPedido() }

        cargarPlatos()
        cargarPedidos()
    }

    private fun cargarPlatos() {
        val call = RetrofitInstance.platoApi.getPlatos()
        call.enqueue(object : Callback<List<Plato>> {
            override fun onResponse(call: Call<List<Plato>>, response: Response<List<Plato>>) {
                if (response.isSuccessful) {
                    listaPlatos = response.body() ?: emptyList()
                    val nombres = listaPlatos.map { it.nombre }
                    val adapter = ArrayAdapter(this@PedidoActivity, android.R.layout.simple_spinner_item, nombres)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnPlato.adapter = adapter
                } else {
                    Toast.makeText(applicationContext, "❌ Error al cargar platos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Plato>>, t: Throwable) {
                Toast.makeText(applicationContext, "⚠️ Error de conexión al cargar platos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarPedidos() {
        val call = RetrofitInstance.pedidosApi.getPedidos()
        call.enqueue(object : Callback<List<Pedido>> {
            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                if (response.isSuccessful) {
                    val listaPedidos = response.body() ?: emptyList()
                    adapter = PedidoAdapter(listaPedidos)
                    recycler.adapter = adapter
                } else {
                    Toast.makeText(applicationContext, "❌ Error al obtener pedidos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                Toast.makeText(applicationContext, "⚠️ Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun guardarPedido() {
        val numeroMesa = edtNumeroMesa.text.toString()
        val comentario = edtComentario.text.toString()
        val cantidadStr = edtCantidad.text.toString()
        val pagado = chkPagado.isChecked
        val estado = spnEstado.selectedItemPosition

        if (numeroMesa.isEmpty() || cantidadStr.isEmpty()) {
            Toast.makeText(this, "⚠️ Ingrese todos los datos requeridos", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = cantidadStr.toIntOrNull()
        if (cantidad == null || cantidad <= 0) {
            Toast.makeText(this, "⚠️ Cantidad no válida", Toast.LENGTH_SHORT).show()
            return
        }

        val platoSeleccionado = listaPlatos.getOrNull(spnPlato.selectedItemPosition)
        if (platoSeleccionado == null) {
            Toast.makeText(this, "⚠️ Seleccione un plato", Toast.LENGTH_SHORT).show()
            return
        }

        val detalle = PedidoDetalle(
            platoId = platoSeleccionado.id,
            cantidad = cantidad
        )

        val pedido = Pedido(
            numeroMesa = numeroMesa,
            fecha = "2025-10-21",
            estado = estado,
            pagado = pagado,
            comentario = comentario,
            mozoId = 1,
            detalles = listOf(detalle)
        )

        val call = RetrofitInstance.pedidosApi.postPedido(pedido)
        call.enqueue(object : Callback<Pedido> {
            override fun onResponse(call: Call<Pedido>, response: Response<Pedido>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "✅ Pedido guardado correctamente", Toast.LENGTH_SHORT).show()
                    cargarPedidos()
                } else {
                    Toast.makeText(applicationContext, "❌ Error al guardar pedido", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Pedido>, t: Throwable) {
                Toast.makeText(applicationContext, "⚠️ Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
