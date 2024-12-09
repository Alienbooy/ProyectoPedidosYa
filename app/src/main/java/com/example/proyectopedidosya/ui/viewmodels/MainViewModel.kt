package com.example.proyectopedidosya.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectopedidosya.repositories.PreferencesRepository
import com.example.proyectopedidosya.repositories.UserRepository
import com.example.proyectopedidosya.ui.activities.RestaurantListActivity
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    /**
     * Maneja el login del usuario
     */
    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch {
            UserRepository.doLogin(
                email,
                password,
                context,
                success = {
                    if (it == null) {
                        _errorMessage.postValue("Usuario o contraseña incorrectos")
                        _isSuccess.postValue(false)
                        return@doLogin
                    }
                    _errorMessage.postValue("")
                    Log.d("MainViewModel", "Token: ${it.access_token}")
                    val token: String = it.access_token!!
                    PreferencesRepository.saveToken(token, context)

                    // Redirige al RestaurantListActivity
                    val intent = Intent(context, RestaurantListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)

                    _isSuccess.postValue(true)
                },
                failure = {
                    _errorMessage.postValue("Error al iniciar sesión: ${it.message}")
                    _isSuccess.postValue(false)
                    it.printStackTrace()
                }
            )
        }
    }

    /**
     * Maneja el registro del usuario
     */
    fun register(name: String, email: String, password: String, role: Int, context: Context) {
        viewModelScope.launch {
            UserRepository.registerUser(
                name = name,
                email = email,
                password = password,
                role = role,
                context = context,
                success = { response ->
                    if (response == null) {
                        _errorMessage.postValue("Error al registrar usuario")
                        _isSuccess.postValue(false)
                        return@registerUser
                    }
                    _errorMessage.postValue("Registro exitoso")
                    PreferencesRepository.saveToken(response.access_token ?: "", context)
                    Log.d("MainViewModel", "Token guardado: ${response.access_token}")
                    _isSuccess.postValue(true)
                },
                failure = { throwable ->
                    _errorMessage.postValue("Error al registrar usuario: ${throwable.message}")
                    _isSuccess.postValue(false)
                    throwable.printStackTrace()
                }
            )
        }
    }
}
