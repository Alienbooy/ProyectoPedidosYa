package com.example.proyectopedidosya.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectopedidosya.databinding.ItemProductBinding
import com.example.proyectopedidosya.models.dto.Producto

class ProductAdapter(
    private val onAddToCartClick: (Producto) -> Unit // Callback para manejar el clic en "Agregar al Carrito"
) : ListAdapter<Producto, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Producto, onAddToCartClick: (Producto) -> Unit) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = "$${product.price}"

            // Cargar la imagen del producto
            Glide.with(binding.root.context)
                .load(product.image)
                .into(binding.imgProductImage)

            // Manejar el clic del botón "Agregar al Carrito"
            binding.btnAddToCart.setOnClickListener {
                onAddToCartClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), onAddToCartClick) // Pasar el callback a la vista
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Producto>() {
        override fun areItemsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Producto, newItem: Producto): Boolean {
            return oldItem == newItem
        }
    }
}
