package com.example.bepresent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.lifecycleScope
import com.example.bepresent.ui.navigation.AppNavigation
import com.example.bepresent.ui.theme.BePresentTheme
import com.example.bepresent.viewmodel.AuthViewModel
import com.example.bepresent.database.dbManager.DatabaseManager
import com.example.bepresent.database.utils.TestDataPopulator
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ініціалізація бази даних
        val database = DatabaseManager.getDatabase(this)

        // Заповнення тестовими даними
        lifecycleScope.launch {
            val testDataPopulator = TestDataPopulator(database)
            testDataPopulator.populateWithTestData()
        }

        setContent {
            BePresentTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val authViewModel: AuthViewModel = viewModel()

                    AppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        database = database,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}