package com.example.bepresent.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bepresent.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginError by authViewModel.loginError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo/Title
        Text(
            text = "BePresent",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Увійдіть до свого акаунту",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                authViewModel.clearError()
            },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                authViewModel.clearError()
            },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true
        )

        // Error Message
        loginError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Forgot Password
        TextButton(
            onClick = { /* TODO: Implement forgot password */ },
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text("Забули пароль?")
        }

        // Login Button
        Button(
            onClick = {
                if (authViewModel.validateLogin(email, password)) {
                    onLoginSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 16.dp)
        ) {
            Text("Увійти", fontSize = 16.sp)
        }

        // Social Login Buttons
        OutlinedButton(
            onClick = { /* TODO: Implement Google login */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 8.dp)
        ) {
            Text("Увійти через Google")
        }

        OutlinedButton(
            onClick = { /* TODO: Implement Instagram login */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 32.dp)
        ) {
            Text("Увійти через Instagram")
        }

        // Sign Up Link
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Не маєте акаунт? ",
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = onNavigateToSignUp) {
                Text("Зареєструйтесь!")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer Links
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = { /* TODO: Terms of Service */ }) {
                Text("Умови використання", fontSize = 12.sp)
            }
            TextButton(onClick = { /* TODO: Privacy Policy */ }) {
                Text("Політика конфіденційності", fontSize = 12.sp)
            }
            TextButton(onClick = { /* TODO: Contact Us */ }) {
                Text("Зв'язатися з нами", fontSize = 12.sp)
            }
        }
    }
}