package com.example.bepresent.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bepresent.database.dbManager.DatabaseManager
import com.example.bepresent.database.room.GiftRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AddGiftUiState(
    val giftName: String = "",
    val description: String = "",
    val link: String = "",
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val giftNameError: String? = null
)

class AddGiftViewModel(
    private val database: DatabaseManager,
    private val boardId: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddGiftUiState())
    val uiState: StateFlow<AddGiftUiState> = _uiState.asStateFlow()

    fun updateGiftName(name: String) {
        _uiState.value = _uiState.value.copy(
            giftName = name,
            giftNameError = if (name.isBlank()) "Назва подарунка не може бути порожньою" else null
        )
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun updateLink(link: String) {
        _uiState.value = _uiState.value.copy(link = link)
    }

    fun showImagePicker() {
        // TODO: Implement image picker logic
        // For now, just simulate selecting an image
        _uiState.value = _uiState.value.copy(
            selectedImageUri = Uri.parse("content://mock/image")
        )
    }

    fun removeImage() {
        _uiState.value = _uiState.value.copy(selectedImageUri = null)
    }

    private fun validateInput(): Boolean {
        val currentState = _uiState.value
        var isValid = true
        var giftNameError: String? = null

        if (currentState.giftName.isBlank()) {
            giftNameError = "Назва подарунка не може бути порожньою"
            isValid = false
        } else if (currentState.giftName.length < 2) {
            giftNameError = "Назва подарунка повинна містити мінімум 2 символи"
            isValid = false
        }

        _uiState.value = currentState.copy(giftNameError = giftNameError)
        return isValid
    }

    fun saveGift(onComplete: (Boolean) -> Unit) {
        if (!validateInput()) {
            onComplete(false)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val newGift = GiftRoom(
                    giftName = _uiState.value.giftName.trim(),
                    giftDescription = _uiState.value.description.trim(),
                    link = _uiState.value.link.trim(),
                    image = null, // TODO: Convert Uri to Bitmap if needed
                    reserved = false,
                    reservoirUserId = null
                )

                database.giftDao().insertGift(newGift)
                onComplete(true)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Помилка при збереженні подарунка: ${e.message}"
                )
                onComplete(false)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}