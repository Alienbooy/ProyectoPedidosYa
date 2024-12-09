package com.example.proyectopedidosya.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectopedidosya.databinding.ItemRestaurantBinding
import com.example.proyectopedidosya.models.dto.Restaurant
import com.example.proyectopedidosya.ui.activities.ProductListActivity

class RestaurantAdapter(private val onClick: (Restaurant) -> Unit) : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(RestaurantDiffCallback()) {

    // ViewHolder para enlazar datos con vistas
    class RestaurantViewHolder(private val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant, onClick: (Restaurant) -> Unit) {
            binding.tvRestaurantName.text = restaurant.name

            // Usa directamente el valor de restaurant.logo como URL
            Glide.with(binding.root.context)
                .load(restaurant.logo)
                .into(binding.imgRestaurantLogo)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, ProductListActivity::class.java)
                intent.putExtra("restaurant_id", restaurant.id) // Pasamos el ID del restaurante
                intent.putExtra("restaurant_name", restaurant.name) // Pasamos el nombre (opcional)
                context.startActivity(intent)
            }
        }
    }

    // Crea una nueva instancia del ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRestaurantBinding.inflate(layoutInflater, parent, false)
        return RestaurantViewHolder(binding)
    }

    // Asigna datos al ViewHolder
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
    }

    // DiffUtil para mejorar el rendimiento del RecyclerView
    class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id // Compara IDs únicos
        }

        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem // Compara el contenido completo
        }
    }
}
