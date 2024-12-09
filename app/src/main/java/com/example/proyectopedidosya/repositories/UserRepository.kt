package com.example.proyectopedidosya.repositories

import android.content.Context
import android.util.Log
import com.example.proyectopedidosya.api.APIProyecto
import com.example.proyectopedidosya.models.dto.LoginRequestDTO
import com.example.proyectopedidosya.models.dto.LoginResponseDTO
import com.example.proyectopedidosya.models.dto.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.log

object UserRepository {

    /**
     * Manejo de inicio de sesión
     */
    suspend fun doLogin(
        email: String,
        password: String,
        context: Context,
        success: (LoginResponseDTO?) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        val retrofit = RetrofitRepository.getRetrofitInstance(context)
        val service: APIProyecto = retrofit.create(APIProyecto::class.java)

        withContext(Dispatchers.IO) {
            try {
                val response = service.login(LoginRequestDTO(email, password))
                success(response)
            } catch (t: Throwable) {
                Log.e("UserRepository", "Error al iniciar sesión: ${t.message}")
                failure(t)
            }
        }
    }

    /**
     * Registro de usuario
     */
    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        role: Int,
        context: Context,
        success: (LoginResponseDTO?) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        val retrofit = RetrofitRepository.getRetrofitInstance(context)
        val service: APIProyecto = retrofit.create(APIProyecto::class.java)

        try {
            val response = withContext(Dispatchers.IO) {
                service.register(RegisterRequest(name, email, password, role))
            }
            Log.d("UserRepository", "Registro exitoso: $response")
            success(response)
        } catch (t: Throwable) {
            Log.e("UserRepository", "Error al registrar usuario: ${t.message}")
            failure(t)
        }
    }

}
