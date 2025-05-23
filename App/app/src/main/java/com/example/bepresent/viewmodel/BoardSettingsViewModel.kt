package com.example.bepresent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bepresent.database.dbManager.DatabaseManager
import com.example.bepresent.database.room.GiftBoardRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class BoardSettingsUiState(
    val boardName: String = "",
    val celebrationDate: Date? = null,
    val accessType: String = "public",
    val description: String = "",
    val showDatePicker: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val boardNameError: String? = null
)

class BoardSettingsViewModel(private val database: DatabaseManager) : ViewModel() {
    private val _uiState = MutableStateFlow(BoardSettingsUiState())
    val uiState: StateFlow<BoardSettingsUiState> = _uiState.asStateFlow()

    fun updateBoardName(name: String) {
        _uiState.value = _uiState.value.copy(
            boardName = name,
            boardNameError = if (name.isBlank()) "Назва дошки не може бути порожньою" else null
        )
    }

    fun updateCelebrationDate(date: Date) {
        _uiState.value = _uiState.value.copy(celebrationDate = date)
    }

    fun updateAccessType(type: String) {
        _uiState.value = _uiState.value.copy(accessType = type)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun showDatePicker() {
        _uiState.value = _uiState.value.copy(showDatePicker = true)
    }

    fun hideDatePicker() {
        _uiState.value = _uiState.value.copy(showDatePicker = false)
    }

    private fun validateInput(): Boolean {
        val currentState = _uiState.value
        var isValid = true
        var boardNameError: String? = null

        if (currentState.boardName.isBlank()) {
            boardNameError = "Назва дошки не може бути порожньою"
            isValid = false
        } else if (currentState.boardName.length < 3) {
            boardNameError = "Назва дошки повинна містити мінімум 3 символи"
            isValid = false
        }

        _uiState.value = currentState.copy(boardNameError = boardNameError)
        return isValid
    }

    fun saveBoard(onComplete: (Boolean) -> Unit) {
        if (!validateInput()) {
            onComplete(false)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val newBoard = GiftBoardRoom(
                    boardName = _uiState.value.boardName.trim(),
                    celebrationDate = _uiState.value.celebrationDate,
                    accessType = _uiState.value.accessType,
                    description = _uiState.value.description.trim(),
                    creationDate = Date()
                )

                database.giftBoardDao().insertGiftBoard(newBoard)
                onComplete(true)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Помилка при збереженні дошки: ${e.message}"
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