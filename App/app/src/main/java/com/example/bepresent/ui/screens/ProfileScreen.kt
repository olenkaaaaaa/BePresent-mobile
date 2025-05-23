package com.example.bepresent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit
) {
    var username by remember { mutableStateOf("Користувач") }
    var email by remember { mutableStateOf("user@example.com") }
    var dateOfBirth by remember { mutableStateOf<Date?>(null) }
    var gender by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Профіль") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    if (isEditing) {
                        TextButton(
                            onClick = {
                                isEditing = false
                                // TODO: Save profile changes
                            }
                        ) {
                            Text("Зберегти")
                        }
                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Редагувати")
                        }
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
            // Profile Avatar
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Аватар",
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Username
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Ім'я користувача") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                enabled = isEditing,
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                }
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                enabled = isEditing,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                }
            )

            // Date of Birth
            OutlinedTextField(
                value = dateOfBirth?.let { dateFormat.format(it) } ?: "",
                onValueChange = { },
                label = { Text("Дата народження") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                enabled = isEditing,
                readOnly = true,
                trailingIcon = if (isEditing) {
                    {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Вибрати дату")
                        }
                    }
                } else null,
                leadingIcon = {
                    Icon(Icons.Default.Cake, contentDescription = null)
                }
            )

            // Gender
            if (isEditing) {
                Text(
                    text = "Стать",
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
                            selected = gender == "male",
                            onClick = { gender = "male" }
                        )
                        Text("Чоловік")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = gender == "female",
                            onClick = { gender = "female" }
                        )
                        Text("Жінка")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = gender == "other",
                            onClick = { gender = "other" }
                        )
                        Text("Інше")
                    }
                }
            } else if (gender.isNotEmpty()) {
                OutlinedTextField(
                    value = when (gender) {
                        "male" -> "Чоловік"
                        "female" -> "Жінка"
                        "other" -> "Інше"
                        else -> ""
                    },
                    onValueChange = { },
                    label = { Text("Стать") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    enabled = false,
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    }
                )
            }

            // Interests
            OutlinedTextField(
                value = interests,
                onValueChange = { interests = it },
                label = { Text("Інтереси") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 24.dp),
                enabled = isEditing,
                maxLines = 4,
                placeholder = { Text("Розкажіть про свої хобі та інтереси...") },
                leadingIcon = {
                    Icon(Icons.Default.Interests, contentDescription = null)
                }
            )

            // Statistics Section
            if (!isEditing) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Статистика",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "3",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Дошки",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "15",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Подарунків",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "7",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Зарезервовано",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dateOfBirth?.time ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            dateOfBirth = Date(it)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Вибрати")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Скасувати")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}