package com.example.loloytar.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loloytar.R
import com.example.loloytar.api.RetrofitInstance
import com.example.loloytar.model.Plato
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearPlatoActivity : AppCompatActivity() {

    private lateinit var inputNombre: EditText
    private lateinit var inputPrecio: EditText
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_plato)

        inputNombre = findViewById(R.id.inputNombre)
        inputPrecio = findViewById(R.id.inputPrecio)
        btnGuardar = findViewById(R.id.btnGuardar)
        btnCancelar = findViewById(R.id.btnCancelar)

        btnGuardar.setOnClickListener {
            val nombre = inputNombre.text.toString().trim()
            val precioStr = inputPrecio.text.toString().trim()

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val precio = precioStr.toDoubleOrNull()
            if (precio == null) {
                Toast.makeText(this, "El precio debe ser un número válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoPlato = Plato(
                id = 0, // o null si el backend lo genera
                nombre = nombre,
                precio = precio,
                activo = true
            )

            guardarPlato(nuevoPlato)
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarPlato(plato: Plato) {
        val call = RetrofitInstance.platoApi.postPlato(plato)
        call.enqueue(object : Callback<Plato> {
            override fun onResponse(call: Call<Plato>, response: Response<Plato>) {
                if (response.isSuccessful) {
                    val platoGuardado = response.body()
                    Toast.makeText(this@CrearPlatoActivity, "Plato guardado: ${platoGuardado?.nombre}", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CrearPlatoActivity, "Error del servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Plato>, t: Throwable) {
                Toast.makeText(this@CrearPlatoActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
