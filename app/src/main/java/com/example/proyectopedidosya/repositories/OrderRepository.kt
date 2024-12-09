package com.example.proyectopedidosya.repositories


import android.content.Context
import com.example.proyectopedidosya.api.APIProyecto
import com.example.proyectopedidosya.models.dto.LocationRequest
import com.example.proyectopedidosya.models.dto.OrderRequest
import com.example.proyectopedidosya.models.dto.Pedido
import com.example.proyectopedidosya.repositories.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response

object OrderRepository {

    /**
     * Obtiene una instancia de APIProyecto
     */
    private fun getApi(context: Context): APIProyecto {
        return RetrofitRepository.getRetrofitInstance(context).create(APIProyecto::class.java)
    }

    /**
     * Obtiene todos los pedidos del usuario autenticado
     */
    suspend fun getOrders(context: Context): List<Pedido> {
        val token = "Bearer ${PreferencesRepository.getToken(context)}"
        return withContext(Dispatchers.IO) {
            getApi(context).getOrders(token) // Llama al endpoint con el token
        }
    }

    /**
     * Crea un nuevo pedido
     */
    suspend fun createOrder(context: Context, request: OrderRequest): Boolean {
        val token = "Bearer ${PreferencesRepository.getToken(context)}"
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<Unit> = getApi(context).createOrder(token, request)
                response.isSuccessful
            } catch (e: Exception) {
                throw Exception("Error al crear el pedido: ${e.message}")
            }
        }
    }

    /**
     * Acepta un pedido
     */
    suspend fun getOrderDetails(context: Context, orderId: Int): Pedido {
        val retrofit = RetrofitRepository.getRetrofitInstance(context)
        val api = retrofit.create(APIProyecto::class.java)
        val token = PreferencesRepository.getToken(context) ?: ""
        return api.getOrderDetails("Bearer $token", orderId)
    }


    /**
     * Obtiene la ubicación actual del chofer para un pedido
     */
    suspend fun getDriverLocation(context: Context, orderId: Int): LocationRequest {
        val token = "Bearer ${PreferencesRepository.getToken(context)}"
        return withContext(Dispatchers.IO) {
            try {
                getApi(context).getDriverLocation(token, orderId)
            } catch (e: Exception) {
                throw Exception("Error al obtener la ubicación del chofer: ${e.message}")
            }
        }
    }

    suspend fun markOrderOnTheWay(context: Context, orderId: Int): Boolean {
        val token = "Bearer ${PreferencesRepository.getToken(context)}"
        return RetrofitRepository.getRetrofitInstance(context)
            .create(APIProyecto::class.java)
            .markOrderOnTheWay(token, orderId).isSuccessful
    }

    suspend fun markOrderDelivered(context: Context, orderId: Int): Boolean {
        val token = "Bearer ${PreferencesRepository.getToken(context)}"
        return RetrofitRepository.getRetrofitInstance(context)
            .create(APIProyecto::class.java)
            .markOrderDelivered(token, orderId).isSuccessful
    }

}

