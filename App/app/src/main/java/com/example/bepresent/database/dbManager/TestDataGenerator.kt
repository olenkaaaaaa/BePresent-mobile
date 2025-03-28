package com.example.bepresent.database.dbManager

import com.example.bepresent.database.room.GiftBoardRoom
import com.example.bepresent.database.room.GiftRoom
import com.example.bepresent.database.room.UserRoom
import kotlin.random.Random

abstract class TestDataGenerator {
    companion object {
        fun generateTestUsers(dataCount: Int): List<UserRoom> {
            // Generate Users
            val users = mutableListOf<UserRoom>()
            val hobbies = listOf("Gaming", "Reading", "Traveling", "Cooking", "Music", "Sports")
            // Auto generate
            for (i in 1..dataCount){
                val randomHobbies = List(Random.nextInt(1, 4)) { hobbies.random() }
                users.add(UserRoom(0, "User$i", "user$i@example.com", "pass"))
            }
            return users;
        }

        fun generateTestGifts(dataCount: Int): List<GiftRoom> {
            val gifts = mutableListOf<GiftRoom>()
            for (i in 1..dataCount){
                gifts.add(GiftRoom(0, "Gift$i", "gift description", "https://hello.world"))
            }
            return gifts
        }

        fun generateTestGiftBoards(dataCount: Int): List<GiftBoardRoom> {
            val boards = mutableListOf<GiftBoardRoom>()
            for (i in 1..dataCount){
                boards.add(GiftBoardRoom(0, 0, "board$i", null, null, "public", "descr."))
            }
            return boards
        }
    }
}