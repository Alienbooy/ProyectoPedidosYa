package com.example.proyectopedidosya.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopedidosya.models.dto.Pedido
import com.example.proyectopedidosya.repositories.OrderRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class OrderStatusViewModel(private val context: Context) : ViewModel() {
    private val _orderStatus = MutableLiveData<Int>()
    val orderStatus: LiveData<String> get() = _transformedOrderStatus

    private val _transformedOrderStatus = MutableLiveData<String>()

    init {
        _orderStatus.observeForever { status ->
            _transformedOrderStatus.value = when (status) {
                1 -> "Estado del Pedido: Solicitado"
                2 -> "Estado del Pedido: Aceptado por chofer"
                3 -> "Estado del Pedido: En camino"
                4 -> "Estado del Pedido: Entregado"
                else -> "Estado desconocido"
            }
        }
    }

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun loadOrderStatus(orderId: Int) {
        viewModelScope.launch {
            try {
                val order = OrderRepository.getOrderDetails(context, orderId)
                _orderStatus.postValue(order.status)
            } catch (e: Exception) {
                _errorMessage.postValue("Error al cargar el pedido: ${e.message}")
            }
        }
    }
}





