package com.example.bepresent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bepresent.database.dbManager.DatabaseManager
import com.example.bepresent.database.room.GiftBoardRoom
import com.example.bepresent.database.room.GiftRoom
import com.example.bepresent.database.room.ReservedGiftsRoom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class GiftBoardUiState(
    val board: GiftBoardRoom? = null,
    val gifts: List<GiftRoom> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class GiftBoardViewModel(
    private val database: DatabaseManager,
    private val boardId: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow(GiftBoardUiState())
    val uiState: StateFlow<GiftBoardUiState> = _uiState.asStateFlow()

    init {
        loadBoard()
        loadGifts()
        addTestGiftsIfEmpty()
    }

    fun loadBoard() {
        viewModelScope.launch {
            try {
                val boards = database.giftBoardDao().getAllGiftBoard()
                val board = boards.find { it.boardId == boardId }
                _uiState.value = _uiState.value.copy(board = board)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Помилка завантаження дошки: ${e.message}"
                )
            }
        }
    }

    fun loadGifts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val allGifts = database.giftDao().getAllGifts()
                // У реальному додатку тут би був зв'язок через boardId
                _uiState.value = _uiState.value.copy(
                    gifts = allGifts,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Помилка завантаження подарунків: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private fun addTestGiftsIfEmpty() {
        viewModelScope.launch {
            try {
                val existingGifts = database.giftDao().getAllGifts()
                if (existingGifts.isEmpty()) {
                    val testGifts = listOf(
                        GiftRoom(
                            giftName = "Нова книга з фантастики",
                            giftDescription = "Останній роман улюбленого автора",
                            link = "https://example.com/book",
                            reserved = false
                        ),
                        GiftRoom(
                            giftName = "Бездротові навушники",
                            giftDescription = "Якісні навушники для музики та дзвінків",
                            link = "https://example.com/headphones",
                            reserved = false
                        ),
                        GiftRoom(
                            giftName = "Кавова чашка з принтом",
                            giftDescription = "Персоналізована чашка з моїм улюбленим котом",
                            link = "https://example.com/mug",
                            reserved = true,
                            reservoirUserId = 1
                        ),
                        GiftRoom(
                            giftName = "Настільна гра",
                            giftDescription = "Цікава стратегічна гра для компанії друзів",
                            link = "https://example.com/boardgame",
                            reserved = false
                        ),
                        GiftRoom(
                            giftName = "Рослина для дому",
                            giftDescription = "Невибаглива кімнатна рослина для затишку",
                            link = "https://example.com/plant",
                            reserved = false
                        )
                    )

                    testGifts.forEach { gift ->
                        database.giftDao().insertGift(gift)
                    }

                    loadGifts()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun reserveGift(giftId: Int) {
        viewModelScope.launch {
            try {
                val gifts = database.giftDao().getAllGifts()
                val gift = gifts.find { it.giftId == giftId }

                if (gift != null && !gift.reserved) {
                    val updatedGift = gift.copy(
                        reserved = true,
                        reservoirUserId = 1 // ID поточного користувача
                    )
                    database.giftDao().insertGift(updatedGift)

                    // Додаємо до списку зарезервованих подарунків
                    val reservedGift = ReservedGiftsRoom(
                        reservationDate = Date(),
                        receiverId = boardId,
                        receiverName = _uiState.value.board?.boardName ?: "Невідомо",
                        reservedGiftName = gift.giftName,
                        reservedGiftDescription = gift.giftDescription,
                        link = gift.link,
                        image = gift.image
                    )
                    database.reservedGiftsDao().insertReservedGift(reservedGift)

                    loadGifts() // Оновлюємо список
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Помилка резервування подарунка: ${e.message}"
                )
            }
        }
    }

    fun refreshData() {
        loadBoard()
        loadGifts()
        clearError()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}