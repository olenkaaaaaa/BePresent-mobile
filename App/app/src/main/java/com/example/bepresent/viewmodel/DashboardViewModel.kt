package com.example.bepresent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bepresent.database.dbManager.DatabaseManager
import com.example.bepresent.database.room.GiftBoardRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class DashboardViewModel(private val database: DatabaseManager) : ViewModel() {
    private val _boards = MutableStateFlow<List<GiftBoardRoom>>(emptyList())
    val boards: StateFlow<List<GiftBoardRoom>> = _boards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadBoards()
        // Додаємо тестові дані при першому запуску
        addTestDataIfEmpty()
    }

    fun loadBoards() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val boardsList = database.giftBoardDao().getAllGiftBoard()
                _boards.value = boardsList
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun addTestDataIfEmpty() {
        viewModelScope.launch {
            try {
                val existingBoards = database.giftBoardDao().getAllGiftBoard()
                if (existingBoards.isEmpty()) {
                    // Додаємо тестові дошки
                    val testBoards = listOf(
                        GiftBoardRoom(
                            boardName = "Мій день народження 2025",
                            celebrationDate = Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000), // через місяць
                            accessType = "public",
                            description = "Список подарунків на мій день народження",
                            creationDate = Date()
                        ),
                        GiftBoardRoom(
                            boardName = "Новий рік 2025",
                            celebrationDate = Date(System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000), // через 2 місяці
                            accessType = "friends",
                            description = "Подарунки на Новий рік для сім'ї",
                            creationDate = Date()
                        ),
                        GiftBoardRoom(
                            boardName = "Весілля друзів",
                            celebrationDate = Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000), // через 3 місяці
                            accessType = "private",
                            description = "Ідеї подарунків для весільної пари",
                            creationDate = Date()
                        )
                    )

                    testBoards.forEach { board ->
                        database.giftBoardDao().insertGiftBoard(board)
                    }

                    // Перезавантажуємо список після додавання тестових даних
                    loadBoards()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refreshBoards() {
        loadBoards()
    }
}