package com.example.proyectopedidosya.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyectopedidosya.databinding.ActivityOrderStatusBinding
import com.example.proyectopedidosya.ui.viewmodels.OrderStatusViewModel

class OrderStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderStatusBinding
    private lateinit var viewModel: OrderStatusViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar ViewModel con contexto
        viewModel = OrderStatusViewModel(this)

        // Obtener el ID del pedido desde el Intent
        val orderId = intent.getIntExtra("ORDER_ID", -1)
        if (orderId != -1) {
            viewModel.loadOrderStatus(orderId)
        } else {
            Toast.makeText(this, "Pedido inválido", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Observa el estado del pedido
        viewModel.orderStatus.observe(this) { statusText ->
            binding.tvOrderStatus.text = statusText
            if (statusText == "Estado del Pedido: Entregado") {
                binding.tvDeliveredMessage.text = "Tu pedido ha sido entregado"
                binding.tvDeliveredMessage.visibility = android.view.View.VISIBLE
            }
        }

        // Observa mensajes de error
        viewModel.errorMessage.observe(this) { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}


