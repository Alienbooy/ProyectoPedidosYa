// src/main/java/com/example/loginapi/ui/activities/RestaurantListActivity.kt
package com.example.proyectopedidosya.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectopedidosya.databinding.ActivityRestaurantListBinding
import com.example.proyectopedidosya.ui.adapters.RestaurantAdapter
import com.example.proyectopedidosya.ui.viewmodels.RestaurantViewModel

class RestaurantListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantListBinding
    private lateinit var viewModel: RestaurantViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el ViewModel
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        // Configurar RecyclerView y Adapter
        val adapter = RestaurantAdapter { restaurant ->
            Toast.makeText(this, "Clicked on ${restaurant.name}", Toast.LENGTH_SHORT).show()
        }
        binding.rvRestaurants.layoutManager = LinearLayoutManager(this)
        binding.rvRestaurants.adapter = adapter

        binding.VerPedido.setOnClickListener {
            val intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
        }

        binding.VerCarrito.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }


        // Observa la lista de restaurantes
        viewModel.restaurants.observe(this) { restaurants ->
            Log.d("RestaurantListActivity", "Observing restaurants: ${restaurants.size}")
            adapter.submitList(restaurants)
        }

        // Observa mensajes de error
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Log.e("RestaurantListActivity", "Error message observed: $errorMessage")
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Carga la lista de restaurantes
        Log.d("RestaurantListActivity", "Loading restaurants...")
        viewModel.loadRestaurants(this)
    }
}
