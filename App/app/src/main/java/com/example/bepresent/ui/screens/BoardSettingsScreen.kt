package com.example.bepresent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bepresent.database.dbManager.DatabaseManager
import com.example.bepresent.viewmodel.BoardSettingsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardSettingsScreen(
    database: DatabaseManager,
    onNavigateBack: () -> Unit,
    onBoardCreated: () -> Unit
) {
    val viewModel: BoardSettingsViewModel = viewModel { BoardSettingsViewModel(database) }
    val uiState by viewModel.uiState.collectAsState()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Налаштування дошки") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Board Name
            OutlinedTextField(
                value = uiState.boardName,
                onValueChange = viewModel::updateBoardName,
                label = { Text("Назва дошки") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                isError = uiState.boardNameError != null
            )
            uiState.boardNameError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Celebration Date
            OutlinedTextField(
                value = uiState.celebrationDate?.let { dateFormat.format(it) } ?: "",
                onValueChange = { },
                label = { Text("Дата святкування") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { viewModel.showDatePicker() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Вибрати дату")
                    }
                }
            )

            // Access Type
            Text(
                text = "Тип доступу",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = uiState.accessType == "public",
                        onClick = { viewModel.updateAccessType("public") }
                    )
                    Text("Публічний - всі можуть переглядати")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = uiState.accessType == "friends",
                        onClick = { viewModel.updateAccessType("friends") }
                    )
                    Text("Тільки для друзів")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = uiState.accessType == "private",
                        onClick = { viewModel.updateAccessType("private") }
                    )
                    Text("Приватний - тільки для мене")
                }
            }

            // Description
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::updateDescription,
                label = { Text("Опис (необов'язково)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 24.dp),
                maxLines = 4
            )

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Скасувати")
                }

                Button(
                    onClick = {
                        viewModel.saveBoard { success ->
                            if (success) {
                                onBoardCreated()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Зберегти")
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

    // Date Picker Dialog
    if (uiState.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.celebrationDate?.time ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDateSelected = { dateMillis ->
                dateMillis?.let { viewModel.updateCelebrationDate(Date(it)) }
                viewModel.hideDatePicker()
            },
            onDismiss = { viewModel.hideDatePicker() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis) }) {
                Text("Вибрати")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Скасувати")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}