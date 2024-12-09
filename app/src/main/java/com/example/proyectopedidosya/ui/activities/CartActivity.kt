package com.example.proyectopedidosya.ui.activities


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectopedidosya.databinding.ActivityCartBinding
import com.example.proyectopedidosya.models.dto.Cart
import com.example.proyectopedidosya.models.dto.DetallePedido
import com.example.proyectopedidosya.models.dto.OrderRequest
import com.example.proyectopedidosya.repositories.OrderRepository
import com.example.proyectopedidosya.ui.adapters.CartAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CartAdapter
    private var currentRestaurantId: Int? = null // Rastrea el restaurante del carrito

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = CartAdapter(Cart.getItems())
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = adapter

        // Configurar botón para realizar el pedido
        binding.btnContinue.setOnClickListener { createOrder() }

        // Actualizar el precio total
        updateTotalPrice()
    }

    private fun createOrder() {
        val address = binding.etAddress.text.toString().trim()

        if (address.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese una dirección", Toast.LENGTH_SHORT).show()
            return
        }

        val orderRequest = OrderRequest(
            restaurant_id = currentRestaurantId ?: -1,
            total = Cart.getTotalPrice(),
            address = address,
            latitude = -17.768779252391017, // Coordenadas de ejemplo
            longitude = -63.182890416715466, // Coordenadas de ejemplo
            details = Cart.getItems().map {
                DetallePedido(
                    product_id = it.product.id,
                    qty = it.quantity,
                    price = it.product.price
                )
            }
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val success = OrderRepository.createOrder(this@CartActivity, orderRequest)
                withContext(Dispatchers.Main) {
                    if (success) {
                        Toast.makeText(this@CartActivity, "Pedido creado exitosamente", Toast.LENGTH_SHORT).show()
                        Cart.clear()
                        currentRestaurantId = null // Resetear el restaurante al limpiar el carrito
                        finish()
                    } else {
                        Toast.makeText(this@CartActivity, "Error al crear el pedido", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CartActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateTotalPrice() {
        binding.tvTotalPrice.text = "Total a pagar: ${Cart.getTotalPrice()} Bs"
    }
}

