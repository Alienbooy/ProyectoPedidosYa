package com.example.proyectopedidosya.repositories

import android.content.Context
import com.example.proyectopedidosya.api.APIProyecto
import com.example.proyectopedidosya.models.dto.Producto
import com.example.proyectopedidosya.models.dto.Restaurant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RestaurantRepository {

    private val api: APIProyecto

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://proyectodelivery.jmacboy.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(APIProyecto::class.java)
    }


    suspend fun getRestaurants(context: Context): List<Restaurant> {
        val token = "Bearer ${PreferencesRepository.getToken(context)}"
        return api.getRestaurants(token)
    }

    suspend fun getProductsByRestaurant(token: String, restaurantId: Int): List<Producto> {
        return api.getRestaurantById(token, restaurantId).products ?: emptyList()
    }

}