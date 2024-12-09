package com.example.proyectopedidosya.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectopedidosya.databinding.ItemCartBinding
import com.example.proyectopedidosya.models.dto.Cart
import com.example.proyectopedidosya.models.dto.CartItem

class CartAdapter(private val items: List<CartItem>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // ViewHolder para los elementos del carrito
    class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem, onQuantityChanged: (Int) -> Unit, onRemove: () -> Unit) {
            // Configurar los datos del producto en la vista
            binding.tvProductName.text = cartItem.product.name
            binding.tvProductPrice.text = "$${cartItem.product.price}"
            binding.tvProductQuantity.text = "Cantidad: ${cartItem.quantity}"

            // Botón para incrementar la cantidad
            binding.btnIncrease.setOnClickListener {
                onQuantityChanged(cartItem.quantity + 1)
            }

            // Botón para disminuir la cantidad
            binding.btnIncrease.setOnClickListener {
                if (cartItem.quantity > 1) {
                    onQuantityChanged(cartItem.quantity - 1)
                }
            }

            // Botón para eliminar el producto del carrito
            binding.btnRemove.setOnClickListener {
                onRemove()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    // Función que se encarga de actualizar la vista con los datos del carrito
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = items[position] // Obtener el ítem del carrito
        holder.bind(
            cartItem,
            onQuantityChanged = { newQuantity ->
                // Actualizar la cantidad del producto en el carrito
                Cart.updateQuantity(cartItem.product.id, newQuantity)
                notifyItemChanged(position) // Actualizar solo este ítem en la vista
            },
            onRemove = {
                // Eliminar el producto del carrito
                Cart.removeItem(cartItem.product.id)
                notifyDataSetChanged() // Actualizar la vista completa
            }
        )
    }

    // Obtener el tamaño del carrito
    override fun getItemCount(): Int = items.size
}
