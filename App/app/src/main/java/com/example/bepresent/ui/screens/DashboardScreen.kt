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
import com.example.bepresent.database.room.GiftBoardRoom
import com.example.bepresent.viewmodel.DashboardViewModel
import com.example.bepresent.ui.components.BoardCard
import com.example.bepresent.ui.components.TopAppBarWithMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    database: DatabaseManager,
    onNavigateToProfile: () -> Unit,
    onNavigateToBoardSettings: () -> Unit,
    onNavigateToGiftBoard: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: DashboardViewModel = viewModel { DashboardViewModel(database) }
    val boards by viewModel.boards.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadBoards()
    }

    Scaffold(
        topBar = {
            TopAppBarWithMenu(
                title = "BePresent",
                showMenu = showMenu,
                onMenuToggle = { showMenu = !showMenu },
                onProfileClick = onNavigateToProfile,
                onLogoutClick = {
                    showMenu = false
                    onLogout()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToBoardSettings,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Створити дошку")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Welcome Section
            Text(
                text = "Ваші дошки подарунків",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (boards.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CardGiftcard,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "У вас ще немає дошок",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Створіть свою першу дошку подарунків!",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onNavigateToBoardSettings,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Створити дошку")
                    }
                }
            } else {
                // Boards List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(boards) { board ->
                        BoardCard(
                            board = board,
                            onClick = { onNavigateToGiftBoard(board.boardId) }
                        )
                    }
                }
            }
        }
    }
}