package com.example.proyectopedidosya.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectopedidosya.repositories.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val role = MutableLiveData(1) //defecto: cliente = 1

    private val _registrationSuccess = MutableLiveData(false)
    val registrationSuccess: LiveData<Boolean> get() = _registrationSuccess

    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String> get() = _errorMessage

    fun register() {
        // Validar campos antes de registrar
        val inputName = name.value ?: ""
        val inputEmail = email.value ?: ""
        val inputPassword = password.value ?: ""
        val inputRole = role.value ?: 1

        if (validateInputs(inputName, inputEmail, inputPassword, inputRole)) { // Si los campos son válidos
            viewModelScope.launch {
                UserRepository.registerUser(
                    name = inputName,
                    email = inputEmail,
                    password = inputPassword,
                    role = inputRole,
                    context = getApplication(),
                    success = {
                        if (it != null) {
                            _registrationSuccess.postValue(true)
                        } else {
                            _errorMessage.postValue("Error al registrar usuario")
                        }
                    },
                    failure = {
                        _errorMessage.postValue(it.message ?: "Error desconocido")
                    }
                )
            }
        }
    }

    /**
     * Valida los campos antes de enviar la solicitud de registro
     */
    private fun validateInputs(name: String, email: String, password: String, role: Int): Boolean {
        return when {
            name.isBlank() -> {
                _errorMessage.value = "El nombre no puede estar vacío"
                false
            }
            email.isBlank() -> {
                _errorMessage.value = "El email no puede estar vacío"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _errorMessage.value = "El email no tiene un formato válido"
                false
            }
            password.length < 8 -> {
                _errorMessage.value = "La contraseña debe tener al menos 8 caracteres"
                false
            }
            role !in 1..2 -> {
                _errorMessage.value = "El rol debe ser 1 (Cliente) o 2 (Chofer)"
                false
            }
            else -> true
        }
    }
}