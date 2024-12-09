package com.example.proyectopedidosya.models.dto

data class LoginRequestDTO(
    var email: String? = null,
    var password: String? = null
)

data class LoginResponseDTO(
    var access_token: String? = null
)

data class RegisterRequest(
    var name: String,
    var email: String,
    var password: String,
    var role: Int
)

data class LocationRequest(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)

data class OrderRequest(
    var restaurant_id: Int = 0,
    var total: Double = 0.0,
    var address: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var details: List<DetallePedido>? = null
)


data class Restaurant(
    var id: Int,
    var name: String,
    var address: String,
    var latitude: Double,
    var longitude: Double,
    var logo: String,
    var products: List<Producto>? = null
)

data class Producto(
    var id: Int = 0,
    var name: String? = null,
    var description: String? = null,
    var price: Double = 0.0,
    var restaurant_id: Int = 0,
    var image : String
)


data class Pedido(
    var id: Int = 0,
    var user_id: Int = 0,
    var restaurant_id: Int = 0,
    var total: Double = 0.0,
    var fechaHora: String? = null,
    var address: String? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var status: Int = 0,
    var driver_id: Int? = null,
    var details: List<DetallePedido>? = null
)

data class DetallePedido(
    var id: Int = 0,
    var product_id: Int = 0,
    var order_id: Int = 0,
    var qty: Int = 0,
    var price: Double = 0.0
)

data class Chofer(
    var id: Int = 0,
    var user_id: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)


