package com.example.loloytar.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loloytar.R
import com.example.loloytar.adapter.UsuarioAdapter
import com.example.loloytar.api.RetrofitInstance
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class UsuarioActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtLogin: EditText
    private lateinit var edtPassword: EditText
    private lateinit var spnRol: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var recyclerUsuarios: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var bottomNav: BottomNavigationView

    private lateinit var usuarioAdapter: UsuarioAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)

        edtNombre = findViewById(R.id.edtNombreUsuario)
        edtLogin = findViewById(R.id.edtLogin)
        edtPassword = findViewById(R.id.edtPassword)
        spnRol = findViewById(R.id.spnRol)
        btnGuardar = findViewById(R.id.btnGuardarUsuario)
        recyclerUsuarios = findViewById(R.id.recyclerUsuarios)
        bottomNav = findViewById(R.id.bottomNavigationView)
        progressBar = findViewById(R.id.progressBar)

        recyclerUsuarios.layoutManager = LinearLayoutManager(this)

        configurarNavegacionInferior()
        obtenerUsuarios()

        btnGuardar.setOnClickListener {
            guardarUsuario()
        }
    }

    private fun obtenerUsuarios() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val listaUsuarios = RetrofitInstance.usuarioApi.getUsuarios()
                usuarioAdapter = UsuarioAdapter(listaUsuarios)
                recyclerUsuarios.adapter = usuarioAdapter
            } catch (e: Exception) {
                Toast.makeText(this@UsuarioActivity, "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun guardarUsuario() {
        val nombre = edtNombre.text.toString()
        val login = edtLogin.text.toString()
        val password = edtPassword.text.toString()
        val rol = spnRol.selectedItem.toString()

        if (nombre.isEmpty() || login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val nuevoUsuario = mapOf(
                    "nombre" to nombre,
                    "usuarioLogin" to login,
                    "password" to password,
                    "rol" to rol
                )
                RetrofitInstance.usuarioApi.postUsuario(nuevoUsuario)
                Toast.makeText(this@UsuarioActivity, "Usuario guardado correctamente", Toast.LENGTH_SHORT).show()
                obtenerUsuarios()
            } catch (e: Exception) {
                Toast.makeText(this@UsuarioActivity, "Error al guardar usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configurarNavegacionInferior() {
        bottomNav.selectedItemId = R.id.nav_usuarios
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_platos -> {
                    startActivity(android.content.Intent(this, Index::class.java))
                    true
                }
                R.id.nav_pedidos -> {
                    startActivity(android.content.Intent(this, PedidoActivity::class.java))
                    true
                }
                R.id.nav_lista_general -> {
                    startActivity(android.content.Intent(this, Lista_GeneralActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
