package com.example.bepresent.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bepresent.database.room.GiftBoardRoom
import com.example.bepresent.database.room.GiftRoom
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenu(
    title: String,
    showMenu: Boolean,
    onMenuToggle: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            Box {
                IconButton(onClick = onMenuToggle) {
                    Icon(Icons.Default.Menu, contentDescription = "Меню")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { onMenuToggle() }
                ) {
                    DropdownMenuItem(
                        text = { Text("Профіль") },
                        onClick = onProfileClick,
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Вийти") },
                        onClick = onLogoutClick,
                        leadingIcon = { Icon(Icons.Default.ExitToApp, contentDescription = null) }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardCard(
    board: GiftBoardRoom,
    onClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = board.boardName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (board.description.isNotEmpty()) {
                        Text(
                            text = board.description,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    board.celebrationDate?.let { date ->
                        Text(
                            text = "Дата: ${dateFormat.format(date)}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // Access Type Badge
                Surface(
                    color = when (board.accessType) {
                        "public" -> MaterialTheme.colorScheme.primaryContainer
                        "friends" -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.tertiaryContainer
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = when (board.accessType) {
                            "public" -> "Публічний"
                            "friends" -> "Друзі"
                            else -> "Приватний"
                        },
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftCard(
    gift: GiftRoom,
    onReserve: (() -> Unit)? = null,
    onEdit: (() -> Unit)? = null,
    isOwner: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = gift.giftName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (gift.giftDescription.isNotEmpty()) {
                        Text(
                            text = gift.giftDescription,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    if (gift.link.isNotEmpty()) {
                        Text(
                            text = "Посилання: ${gift.link}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                // Action Buttons
                if (isOwner && onEdit != null) {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Редагувати")
                    }
                } else if (!isOwner && onReserve != null) {
                    if (gift.reserved) {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = "Зарезервовано",
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = onReserve,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("Зарезервувати")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyState(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    actionButton: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (actionButton != null) {
            Spacer(modifier = Modifier.height(24.dp))
            actionButton()
        }
    }
}