package com.example.proyectopedidosya.models.dto


data class CartItem(
    val product: Producto,
    var quantity: Int = 1
)
object Cart {
    private val items = mutableListOf<CartItem>()
    private var currentRestaurantId: Int? = null

    /**
     * Agregar un producto al carrito
     */
    fun addItem(product: Producto, quantity: Int): Boolean {
        if (currentRestaurantId == null) {
            // Si el carrito está vacío, establece el restaurante actual
            currentRestaurantId = product.restaurant_id
        } else if (currentRestaurantId != product.restaurant_id) {
            // Si el producto pertenece a otro restaurante, rechaza la operación
            return false
        }

        val existingItem = items.find { it.product.id == product.id }
        if (existingItem != null) {
            // Si el producto ya existe, actualiza la cantidad
            existingItem.quantity += quantity
        } else {
            // Si no existe, agrega un nuevo ítem
            items.add(CartItem(product, quantity))
        }
        return true
    }

    /**
     * Vaciar el carrito
     */
    fun clear() {
        items.clear()
        currentRestaurantId = null
    }

    // Actualizar la cantidad de un producto en el carrito
    fun updateQuantity(productId: Int, newQuantity: Int) {
        val item = items.find { it.product.id == productId }
        if (item != null) {
            if (newQuantity > 0) {
                item.quantity = newQuantity
            } else {
                // Si la cantidad es 0 o menor, elimina el ítem
                removeItem(productId)
            }
        }
    }

    // Eliminar un producto del carrito
    fun removeItem(productId: Int) {
        items.removeAll { it.product.id == productId }
    }

    fun getItems(): List<CartItem> = items

    fun getTotalPrice(): Double = items.sumOf { it.product.price * it.quantity }
}
