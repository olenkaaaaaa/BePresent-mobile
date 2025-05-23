package com.example.bepresent.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<String?>(null)
    val currentUser: StateFlow<String?> = _currentUser.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    fun login(username: String = "User") {
        _currentUser.value = username
        _isLoggedIn.value = true
        _loginError.value = null
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
        _loginError.value = null
    }

    fun validateLogin(email: String, password: String): Boolean {
        return when {
            email.isBlank() -> {
                _loginError.value = "Email не може бути порожнім"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _loginError.value = "Невірний формат email"
                false
            }
            password.isBlank() -> {
                _loginError.value = "Пароль не може бути порожнім"
                false
            }
            password.length < 6 -> {
                _loginError.value = "Пароль повинен містити мінімум 6 символів"
                false
            }
            else -> {
                _loginError.value = null
                true
            }
        }
    }

    fun validateSignUp(username: String, email: String, password: String, confirmPassword: String): Boolean {
        return when {
            username.isBlank() -> {
                _loginError.value = "Ім'я користувача не може бути порожнім"
                false
            }
            username.length < 3 -> {
                _loginError.value = "Ім'я користувача повинно містити мінімум 3 символи"
                false
            }
            email.isBlank() -> {
                _loginError.value = "Email не може бути порожнім"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _loginError.value = "Невірний формат email"
                false
            }
            password.isBlank() -> {
                _loginError.value = "Пароль не може бути порожнім"
                false
            }
            password.length < 6 -> {
                _loginError.value = "Пароль повинен містити мінімум 6 символів"
                false
            }
            password != confirmPassword -> {
                _loginError.value = "Паролі не співпадають"
                false
            }
            else -> {
                _loginError.value = null
                true
            }
        }
    }

    fun clearError() {
        _loginError.value = null
    }
}