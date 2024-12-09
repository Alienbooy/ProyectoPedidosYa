package com.example.proyectopedidosya.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectopedidosya.databinding.ActivityProductListBinding
import com.example.proyectopedidosya.models.dto.Cart
import com.example.proyectopedidosya.models.dto.Producto
import com.example.proyectopedidosya.repositories.PreferencesRepository
import com.example.proyectopedidosya.repositories.RestaurantRepository
import com.example.proyectopedidosya.ui.adapters.ProductAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtén los datos del intent
        val restaurantId = intent.getIntExtra("restaurant_id", -1)
        val restaurantName = intent.getStringExtra("restaurant_name")

        // Muestra el nombre del restaurante
        binding.tvRestaurantName.text = restaurantName

        // Configura el RecyclerView y su adaptador
        adapter = ProductAdapter { product ->
            addToCart(product)
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter

        // Configura el botón "Ver Carrito"
        binding.VerCarrito.setOnClickListener {
            navigateToCart()
        }

        // Carga los productos
        loadProducts(restaurantId)
    }

    private fun loadProducts(restaurantId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            // Obtiene el token de autenticación
            try {
                val token = "Bearer ${PreferencesRepository.getToken(this@ProductListActivity)}"
                val products = RestaurantRepository.getProductsByRestaurant(token, restaurantId)
                withContext(Dispatchers.Main) {
                    adapter.submitList(products)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ProductListActivity, "Error loading products", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Agrega un producto al carrito
     */
    private fun addToCart(product: Producto) {
        val added = Cart.addItem(product, 1) // Intenta agregar el producto al carrito
        if (added) {
            Toast.makeText(this, "${product.name} agregado al carrito", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Solo puedes agregar productos del mismo restaurante", Toast.LENGTH_SHORT).show()
        }
    }



    /**
     * Navega a la actividad del carrito
     */
    private fun navigateToCart() {
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }
}
