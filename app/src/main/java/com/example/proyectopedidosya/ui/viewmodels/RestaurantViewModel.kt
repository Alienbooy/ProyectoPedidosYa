package com.example.proyectopedidosya.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopedidosya.models.dto.Restaurant
import com.example.proyectopedidosya.repositories.RestaurantRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RestaurantViewModel : ViewModel() {
    private val _restaurants = MutableLiveData<List<Restaurant>>() // LISTA DE RESTAURANTES
    val restaurants: LiveData<List<Restaurant>> get() = _restaurants // OBTIENE LA LISTA DE RESTAURANTES

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun loadRestaurants(context: Context) { // CARGA LOS RESTAURANTES
        viewModelScope.launch {
            try {
                Log.d("RestaurantViewModel", "Loading restaurants...")
                val restaurants = RestaurantRepository.getRestaurants(context) // LLAMADA A LA api PARA OBTENER LOS RESTAURANTES
                _restaurants.value = restaurants
                Log.d("RestaurantViewModel", "Restaurants loaded: ${restaurants.size}")
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _errorMessage.value = "Error: ${e.message()} - $errorBody"
                Log.e("RestaurantViewModel", "HTTP Exception: ${e.message()} - $errorBody")
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage}"
                Log.e("RestaurantViewModel", "Exception: ${e.localizedMessage}")
            }
        }
    }

}