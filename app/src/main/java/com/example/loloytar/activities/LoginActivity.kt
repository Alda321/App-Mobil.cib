package com.example.loloytar.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loloytar.R
import com.example.loloytar.api.RetrofitInstance
import com.example.loloytar.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var edtUsuario: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnIngresar: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val mainLayout = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edtUsuario = findViewById(R.id.editTextText)
        edtPassword = findViewById(R.id.editTextTextPassword)
        btnIngresar = findViewById(R.id.button)
        progressBar = findViewById(R.id.progressBar)

        // Revisar si ya hay sesión
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedRol = sharedPref.getInt("rol", -1)
        val savedNombre = sharedPref.getString("nombre", null)
        if (savedRol != -1 && savedNombre != null) {
            navigateToRole(savedRol)
            finish()
        }

        btnIngresar.setOnClickListener {
            val username = edtUsuario.text.toString()
            val password = edtPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                loginUsuario(username, password)
            }
        }
    }

    private fun loginUsuario(username: String, password: String) {
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.usuarioApi.getUsuarios().execute()
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val usuarios = response.body() ?: emptyList()
                        val usuario = usuarios.find { it.login == username && it.password == password }

                        if (usuario != null) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Bienvenido ${usuario.nombre}",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Guardar sesión
                            val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            sharedPref.edit().apply {
                                putInt("rol", usuario.rol.toIntOrNull() ?: -1)
                                putString("nombre", usuario.nombre)
                                apply()
                            }

                            navigateToRole(usuario.rol.toIntOrNull() ?: -1)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Usuario o contraseña incorrecta",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Error en servidor", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToRole(rol: Int) {
        when (rol) {
            0 -> startActivity(Intent(this, PedidoActivity::class.java)) // Mozo
            1 -> startActivity(Intent(this, CocinaActivity::class.java)) // Cocinero
            2 -> startActivity(Intent(this, PlatoActivity::class.java)) // Admin
            else -> Toast.makeText(this, "Rol no definido", Toast.LENGTH_SHORT).show()
        }
    }
}
