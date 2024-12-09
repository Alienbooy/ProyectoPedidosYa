package com.example.proyectopedidosya.api

import com.example.proyectopedidosya.models.dto.LocationRequest
import com.example.proyectopedidosya.models.dto.LoginRequestDTO
import com.example.proyectopedidosya.models.dto.LoginResponseDTO
import com.example.proyectopedidosya.models.dto.OrderRequest
import com.example.proyectopedidosya.models.dto.Pedido
import com.example.proyectopedidosya.models.dto.Producto
import com.example.proyectopedidosya.models.dto.RegisterRequest
import com.example.proyectopedidosya.models.dto.Restaurant
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface APIProyecto {

    // Autenticación
    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequestDTO): LoginResponseDTO

    @POST("users")
    suspend fun register(@Body registerRequest: RegisterRequest): LoginResponseDTO


    // Restaurantes
    @POST("restaurants")
    suspend fun insertRestaurant(@Header("Authorization") token: String, @Body restaurant: Restaurant): Restaurant

    @GET("restaurants")
    suspend fun getRestaurants(@Header("Authorization") token: String): List<Restaurant>

    @GET("restaurants/{id}")
    suspend fun getRestaurantById(@Header("Authorization") token: String, @Path("id") id: Int): Restaurant


    // Productos
    @GET("restaurants/{id}/products")
    suspend fun getProductsByRestaurant(@Header("Authorization") token: String, @Path("id") restaurantId: Int): List<Producto>

    @POST("products")
    suspend fun createProduct(@Header("Authorization") token: String, @Body product: Producto): Response<Unit>

    // Pedidos
    @GET("orders")
    suspend fun getOrders(@Header("Authorization") token: String): List<Pedido>

    @POST("orders")
    suspend fun createOrder(@Header("Authorization") token: String, @Body request: OrderRequest): Response<Unit>

    @GET("orders/{id}")
    suspend fun getOrderDetails(@Header("Authorization") token: String, @Path("id") orderId: Int): Pedido

    @POST("orders/{id}/omw")
    suspend fun markOrderOnTheWay(@Header("Authorization") token: String, @Path("id") orderId: Int): Response<Unit>

    @POST("orders/{id}/delivered")
    suspend fun markOrderDelivered(@Header("Authorization") token: String, @Path("id") orderId: Int): Response<Unit>

    // Choferes
    @GET("drivers/location")
    suspend fun getDriverLocation(@Header("Authorization") token: String, @Query("orderId") orderId: Int): LocationRequest

    /**
    // Choferes
    @POST("drivers/location")
    suspend fun updateDriverLocation(@Header("Authorization") token: String, @Body location: LocationRequest): Unit

    @GET("orders/free")
    suspend fun getFreeOrders(@Header("Authorization") token: String): List<Pedido>


    @POST("drivers/location")
    suspend fun updateDriverLocation(@Body location: LocationRequest): Response<Unit>
    */
    
}

