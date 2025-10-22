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
import java.text.SimpleDateFormat
import java.util.*

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
    private var listaPedidos: List<Pedido> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)

        // Referencias a vistas
        recycler = findViewById(R.id.recyclerPedidos)
        edtNumeroMesa = findViewById(R.id.edtNumeroMesa)
        edtComentario = findViewById(R.id.edtComentario)
        edtCantidad = findViewById(R.id.edtCantidad)
        chkPagado = findViewById(R.id.chkPagado)
        spnEstado = findViewById(R.id.spnEstado)
        spnPlato = findViewById(R.id.spnPlato)
        btnGuardar = findViewById(R.id.btnGuardarPedido)

        // RecyclerView
        recycler.isNestedScrollingEnabled = false
        recycler.layoutManager = LinearLayoutManager(this)

        btnGuardar.setOnClickListener { guardarPedido() }

        // Cargar datos
        cargarPlatos()
    }

    private fun cargarPlatos() {
        RetrofitInstance.platoApi.getPlatos().enqueue(object : Callback<List<Plato>> {
            override fun onResponse(call: Call<List<Plato>>, response: Response<List<Plato>>) {
                listaPlatos = response.body() ?: emptyList()
                val spinnerAdapter = ArrayAdapter(
                    this@PedidoActivity,
                    android.R.layout.simple_spinner_item,
                    listaPlatos.map { it.nombre }
                )
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spnPlato.adapter = spinnerAdapter

                cargarPedidosConDetalles() // cargar pedidos después de platos
            }
            override fun onFailure(call: Call<List<Plato>>, t: Throwable) {
                cargarPedidosConDetalles()
            }
        })
    }

    private fun cargarPedidosConDetalles() {
        // 1. Obtener pedidos
        RetrofitInstance.pedidosApi.getPedidos().enqueue(object : Callback<List<Pedido>> {
            override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                if (!response.isSuccessful) return
                val pedidos = response.body() ?: emptyList()

                // 2. Obtener detalles
                RetrofitInstance.pedidosDetalleApi.getPedidoDetalles().enqueue(object : Callback<List<PedidoDetalle>> {
                    override fun onResponse(call: Call<List<PedidoDetalle>>, response: Response<List<PedidoDetalle>>) {
                        if (!response.isSuccessful) return
                        val detalles = response.body() ?: emptyList()

                        // 3. Asignar detalles a cada pedido
                        listaPedidos = pedidos.map { pedido ->
                            val detallesDelPedido = detalles.filter { it.pedidoId == pedido.id }
                            pedido.copy(detalles = detallesDelPedido)
                        }

                        // 4. Asignar al adapter
                        adapter = PedidoAdapter(listaPedidos, listaPlatos)
                        recycler.adapter = adapter
                    }
                    override fun onFailure(call: Call<List<PedidoDetalle>>, t: Throwable) {}
                })
            }
            override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {}
        })
    }

    private fun guardarPedido() {
        val numeroMesa = edtNumeroMesa.text.toString()
        val comentario = edtComentario.text.toString()
        val cantidadStr = edtCantidad.text.toString()
        val estado = spnEstado.selectedItemPosition
        val pagado = chkPagado.isChecked
        val platoSeleccionado = listaPlatos.getOrNull(spnPlato.selectedItemPosition) ?: return

        if (numeroMesa.isEmpty() || cantidadStr.isEmpty()) return
        val cantidad = cantidadStr.toIntOrNull() ?: return
        if (cantidad <= 0) return

        val fechaActual = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())
        val pedido = Pedido(
            numeroMesa = numeroMesa,
            fecha = fechaActual,
            estado = estado,
            pagado = pagado,
            comentario = comentario,
            mozoId = 1
        )

        // 1️⃣ Crear el pedido
        RetrofitInstance.pedidosApi.postPedido(pedido).enqueue(object : Callback<Pedido> {
            override fun onResponse(call: Call<Pedido>, response: Response<Pedido>) {
                if (response.isSuccessful) {
                    val pedidoCreado = response.body() ?: return

                    // 2️⃣ Crear el detalle del pedido usando el pedidoId recién creado
                    val detalle = PedidoDetalle(
                        pedidoId = pedidoCreado.id,
                        platoId = platoSeleccionado.id,
                        cantidad = cantidad
                    )

                    RetrofitInstance.pedidosDetalleApi.postPedidoDetalle(detalle)
                        .enqueue(object : Callback<PedidoDetalle> {
                            override fun onResponse(
                                call: Call<PedidoDetalle>,
                                response: Response<PedidoDetalle>
                            ) {
                                limpiarCampos()
                                cargarPedidosConDetalles() // actualizar RecyclerView
                            }

                            override fun onFailure(call: Call<PedidoDetalle>, t: Throwable) {}
                        })
                }
            }

            override fun onFailure(call: Call<Pedido>, t: Throwable) {}
        })
    }


    private fun limpiarCampos() {
        edtNumeroMesa.text.clear()
        edtComentario.text.clear()
        edtCantidad.text.clear()
        chkPagado.isChecked = false
        spnPlato.setSelection(0)
        spnEstado.setSelection(0)
    }
}
