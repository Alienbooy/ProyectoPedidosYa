package com.example.proyectopedidosya.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopedidosya.databinding.ItemOrderBinding
import com.example.proyectopedidosya.models.dto.Pedido

class OrderAdapter(
    private val orders: List<Pedido>,
    private val onOrderClick: (Pedido) -> Unit // funcion que se ejecuta al hacer click en un pedido
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Pedido, onClick: (Pedido) -> Unit) {
            binding.tvOrderId.text = "Pedido #${order.id}"
            binding.tvNameOrder.text = order.address
            binding.tvOrderDate.text = order.fechaHora
            binding.tvOrderTotal.text = "Total: ${order.total} Bs"

        // funcion que se ejecuta al hacer click en un pedido
            binding.root.setOnClickListener {
                onClick(order)
            }
        }
    }
    // funcion que se encarga de crear el view holder para los pedidos
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    // esta duncion se encarga de actualizar la vista con los datos de los pedidos
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position], onOrderClick)
    }

    override fun getItemCount(): Int = orders.size
}

