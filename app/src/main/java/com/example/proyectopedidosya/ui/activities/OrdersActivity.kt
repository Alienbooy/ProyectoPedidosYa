package com.example.proyectopedidosya.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectopedidosya.databinding.ActivityOrdersBinding
import com.example.proyectopedidosya.repositories.OrderRepository
import com.example.proyectopedidosya.ui.adapters.OrderAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        loadOrders()
    }

    private fun setupRecyclerView() {
        binding.rvOrders.layoutManager = LinearLayoutManager(this)
    }

    private fun loadOrders() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val orders = OrderRepository.getOrders(this@OrdersActivity)
                withContext(Dispatchers.Main) {
                    if (orders.isEmpty()) {
                        binding.tvNoOrdersMessage.text = "No hay pedidos disponibles."
                        binding.tvNoOrdersMessage.visibility = View.VISIBLE
                        binding.rvOrders.visibility = View.GONE
                    } else {
                        binding.tvNoOrdersMessage.visibility = View.GONE
                        binding.rvOrders.visibility = View.VISIBLE
                        binding.rvOrders.adapter = OrderAdapter(orders) { order ->
                            // Navegar al OrderStatusActivity
                            val intent = Intent(this@OrdersActivity, OrderStatusActivity::class.java)
                            intent.putExtra("ORDER_ID", order.id) // Pasar el ID del pedido
                            startActivity(intent)
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@OrdersActivity, "Error loading orders: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}

