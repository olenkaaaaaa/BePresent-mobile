package com.example.bepresent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bepresent.database.dbManager.DatabaseManager
import com.example.bepresent.ui.components.GiftCard
import com.example.bepresent.ui.components.EmptyState
import com.example.bepresent.viewmodel.GiftBoardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftBoardScreen(
    database: DatabaseManager,
    boardId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToAddGift: () -> Unit
) {
    val viewModel: GiftBoardViewModel = viewModel { GiftBoardViewModel(database, boardId) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(boardId) {
        viewModel.loadBoard()
        viewModel.loadGifts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.board?.boardName ?: "Завантаження...",
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Оновити")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddGift,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Додати подарунок")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Board Info
            uiState.board?.let { board ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = board.boardName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        if (board.description.isNotEmpty()) {
                            Text(
                                text = board.description,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        board.celebrationDate?.let { date ->
                            Text(
                                text = "Дата: ${java.text.SimpleDateFormat("dd.MM.yyyy", java.util.Locale.getDefault()).format(date)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            ) {
                                Text(
                                    text = when (board.accessType) {
                                        "public" -> "Публічний"
                                        "friends" -> "Друзі"
                                        else -> "Приватний"
                                    },
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = "${uiState.gifts.size} подарунків",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Gifts List
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.gifts.isEmpty()) {
                EmptyState(
                    icon = {
                        Icon(
                            Icons.Default.CardGiftcard,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    title = "Немає подарунків",
                    description = "Додайте свій перший подарунок до списку!",
                    actionButton = {
                        Button(
                            onClick = onNavigateToAddGift,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Додати подарунок")
                        }
                    }
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.gifts) { gift ->
                        GiftCard(
                            gift = gift,
                            onReserve = if (!gift.reserved) {
                                { viewModel.reserveGift(gift.giftId) }
                            } else null,
                            isOwner = true // Поки що вважаємо всіх власниками
                        )
                    }
                }
            }

            // Error Message
            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}