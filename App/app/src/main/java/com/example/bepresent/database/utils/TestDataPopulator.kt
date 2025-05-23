package com.example.bepresent.database.utils

import com.example.bepresent.database.dbManager.DatabaseManager
import com.example.bepresent.database.room.GiftBoardRoom
import com.example.bepresent.database.room.GiftRoom
import com.example.bepresent.database.room.ReservedGiftsRoom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class TestDataPopulator(private val database: DatabaseManager) {

    suspend fun populateWithTestData() = withContext(Dispatchers.IO) {
        try {
            // Перевіряємо чи є вже дані
            val existingBoards = database.giftBoardDao().getAllGiftBoard()
            if (existingBoards.isNotEmpty()) return@withContext

            // Створюємо тестові дошки
            val testBoards = createTestBoards()
            testBoards.forEach { board ->
                database.giftBoardDao().insertGiftBoard(board)
            }

            // Створюємо тестові подарунки
            val testGifts = createTestGifts()
            testGifts.forEach { gift ->
                database.giftDao().insertGift(gift)
            }

            // Створюємо тестові зарезервовані подарунки
            val testReservedGifts = createTestReservedGifts()
            testReservedGifts.forEach { reservedGift ->
                database.reservedGiftsDao().insertReservedGift(reservedGift)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createTestBoards(): List<GiftBoardRoom> {
        val now = System.currentTimeMillis()
        return listOf(
            GiftBoardRoom(
                boardName = "Мій 25-й день народження",
                celebrationDate = Date(now + 30L * 24 * 60 * 60 * 1000), // через місяць
                accessType = "public",
                description = "Список подарунків на мій день народження. Буду радий будь-якому з цих подарунків!",
                creationDate = Date(now - 7L * 24 * 60 * 60 * 1000) // створено тиждень тому
            ),
            GiftBoardRoom(
                boardName = "Новий рік 2025",
                celebrationDate = Date(now + 60L * 24 * 60 * 60 * 1000), // через 2 місяці
                accessType = "friends",
                description = "Подарунки на Новий рік для всієї сім'ї",
                creationDate = Date(now - 14L * 24 * 60 * 60 * 1000) // створено 2 тижні тому
            ),
            GiftBoardRoom(
                boardName = "Весілля Ані та Максима",
                celebrationDate = Date(now + 90L * 24 * 60 * 60 * 1000), // через 3 місяці
                accessType = "private",
                description = "Ідеї подарунків для молодої пари на весілля",
                creationDate = Date(now - 21L * 24 * 60 * 60 * 1000) // створено 3 тижні тому
            ),
            GiftBoardRoom(
                boardName = "8 березня для мами",
                celebrationDate = Date(now + 120L * 24 * 60 * 60 * 1000), // через 4 місяці
                accessType = "friends",
                description = "Подарунок для найкращої мами у світі",
                creationDate = Date(now - 3L * 24 * 60 * 60 * 1000) // створено 3 дні тому
            ),
            GiftBoardRoom(
                boardName = "Випускний 2025",
                celebrationDate = Date(now + 150L * 24 * 60 * 60 * 1000), // через 5 місяців
                accessType = "public",
                description = "Святкування закінчення університету",
                creationDate = Date(now - 1L * 24 * 60 * 60 * 1000) // створено вчора
            )
        )
    }

    private fun createTestGifts(): List<GiftRoom> {
        return listOf(
            GiftRoom(
                giftName = "MacBook Air M2",
                giftDescription = "Новий ноутбук для роботи та навчання. Хотілось би 13-дюймовий у сірому кольорі.",
                link = "https://www.apple.com/macbook-air/",
                reserved = false
            ),
            GiftRoom(
                giftName = "Бездротові навушники AirPods Pro",
                giftDescription = "Якісні навушники з шумозаглушенням для музики та відеодзвінків",
                link = "https://www.apple.com/airpods-pro/",
                reserved = true,
                reservoirUserId = 1
            ),
            GiftRoom(
                giftName = "Книга 'Atomic Habits'",
                giftDescription = "Популярна книга про формування корисних звичок від Джеймса Кліра",
                link = "https://jamesclear.com/atomic-habits",
                reserved = false
            ),
            GiftRoom(
                giftName = "Кавова машина Nespresso",
                giftDescription = "Компактна кавова машина для приготування ароматної кави вдома",
                link = "https://www.nespresso.com/",
                reserved = false
            ),
            GiftRoom(
                giftName = "Рослина Монстера",
                giftDescription = "Красива кімнатна рослина для озеленення квартири. Середнього розміру.",
                link = "Будь-який садовий центр",
                reserved = true,
                reservoirUserId = 2
            ),
            GiftRoom(
                giftName = "Настільна гра 'Колонізатори'",
                giftDescription = "Класична стратегічна настільна гра для компанії друзів",
                link = "https://boardgamegeek.com/boardgame/13/catan",
                reserved = false
            ),
            GiftRoom(
                giftName = "Спортивний рюкзак Nike",
                giftDescription = "Зручний рюкзак для походів до спортзалу та подорожей",
                link = "https://www.nike.com/",
                reserved = false
            ),
            GiftRoom(
                giftName = "Термочашка YETI",
                giftDescription = "Якісна термочашка, яка довго зберігає температуру напоїв",
                link = "https://www.yeti.com/",
                reserved = true,
                reservoirUserId = 3
            ),
            GiftRoom(
                giftName = "Сертифікат на SPA",
                giftDescription = "Сертифікат на розслаблюючі SPA-процедури у центрі міста",
                link = "SPA центр 'Релакс'",
                reserved = false
            ),
            GiftRoom(
                giftName = "Смарт-годинник Apple Watch",
                giftDescription = "Розумний годинник для відстеження активності та здоров'я",
                link = "https://www.apple.com/watch/",
                reserved = false
            ),
            GiftRoom(
                giftName = "Скейтборд",
                giftDescription = "Професійний скейтборд для катання по місту",
                link = "Спортивний магазин",
                reserved = false
            ),
            GiftRoom(
                giftName = "Курс онлайн-програмування",
                giftDescription = "Річний доступ до курсів програмування на Udemy або Coursera",
                link = "https://www.udemy.com/",
                reserved = true,
                reservoirUserId = 4
            ),
            GiftRoom(
                giftName = "Портативна колонка JBL",
                giftDescription = "Бездротова Bluetooth колонка для прослуховування музики",
                link = "https://www.jbl.com/",
                reserved = false
            ),
            GiftRoom(
                giftName = "Набір для приготування суші",
                giftDescription = "Повний набір для приготування суші вдома з інструкцією",
                link = "Кулінарний магазин",
                reserved = false
            ),
            GiftRoom(
                giftName = "Фітнес-браслет Xiaomi",
                giftDescription = "Доступний фітнес-трекер для відстеження кроків та сну",
                link = "https://www.mi.com/",
                reserved = false
            ),
            GiftRoom(
                giftName = "Електрична зубна щітка",
                giftDescription = "Сучасна електрична зубна щітка Oral-B або Philips",
                link = "Аптека або електронний магазин",
                reserved = false
            ),
            GiftRoom(
                giftName = "Абонемент у спортзал",
                giftDescription = "Місячний або річний абонемент у найближчий фітнес-центр",
                link = "Спортзал 'FitLife'",
                reserved = true,
                reservoirUserId = 5
            ),
            GiftRoom(
                giftName = "Пазл на 1000 деталей",
                giftDescription = "Красивий пазл з пейзажем або відомою картиною",
                link = "Магазин іграшок",
                reserved = false
            ),
            GiftRoom(
                giftName = "Ароматична свічка",
                giftDescription = "Натуральна соєва свічка з приємним ароматом лаванди або ванілі",
                link = "Магазин декору",
                reserved = false
            ),
            GiftRoom(
                giftName = "Електронна читалка Kindle",
                giftDescription = "Компактна читалка для електронних книг від Amazon",
                link = "https://www.amazon.com/kindle/",
                reserved = false
            )
        )
    }

    private fun createTestReservedGifts(): List<ReservedGiftsRoom> {
        val now = System.currentTimeMillis()
        return listOf(
            ReservedGiftsRoom(
                reservationDate = Date(now - 5L * 24 * 60 * 60 * 1000), // 5 днів тому
                receiverId = 1,
                receiverName = "Мій 25-й день народження",
                reservedGiftName = "Бездротові навушники AirPods Pro",
                reservedGiftDescription = "Якісні навушники з шумозаглушенням для музики та відеодзвінків",
                link = "https://www.apple.com/airpods-pro/"
            ),
            ReservedGiftsRoom(
                reservationDate = Date(now - 3L * 24 * 60 * 60 * 1000), // 3 дні тому
                receiverId = 1,
                receiverName = "Мій 25-й день народження",
                reservedGiftName = "Рослина Монстера",
                reservedGiftDescription = "Красива кімнатна рослина для озеленення квартири. Середнього розміру.",
                link = "Будь-який садовий центр"
            ),
            ReservedGiftsRoom(
                reservationDate = Date(now - 2L * 24 * 60 * 60 * 1000), // 2 дні тому
                receiverId = 2,
                receiverName = "Новий рік 2025",
                reservedGiftName = "Термочашка YETI",
                reservedGiftDescription = "Якісна термочашка, яка довго зберігає температуру напоїв",
                link = "https://www.yeti.com/"
            ),
            ReservedGiftsRoom(
                reservationDate = Date(now - 1L * 24 * 60 * 60 * 1000), // вчора
                receiverId = 1,
                receiverName = "Мій 25-й день народження",
                reservedGiftName = "Курс онлайн-програмування",
                reservedGiftDescription = "Річний доступ до курсів програмування на Udemy або Coursera",
                link = "https://www.udemy.com/"
            ),
            ReservedGiftsRoom(
                reservationDate = Date(now - 6L * 60 * 60 * 1000), // 6 годин тому
                receiverId = 4,
                receiverName = "8 березня для мами",
                reservedGiftName = "Абонемент у спортзал",
                reservedGiftDescription = "Місячний або річний абонемент у найближчий фітнес-центр",
                link = "Спортзал 'FitLife'"
            )
        )
    }
}