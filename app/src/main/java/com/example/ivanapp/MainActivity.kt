package com.example.ivanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.ivanapp.ui.theme.IvanAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Modern edge-to-edge layout
        enableEdgeToEdge()

        setContent {
            val isDark = isSystemInDarkTheme()
            
            // CRITICAL REQ 1: Navigation Bar & Status Bar Logic
            // If brightness is light (isDark = false), icons should be black. 
            // If brightness is dark (isDark = true), icons should be white.
            SideEffect {
                val window = this.window
                val insetsController = WindowInsetsControllerCompat(window, window.decorView)

                // Status bar colors
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()

                // Icon color logic:
                // isAppearanceLightNavigationBars = true -> Black Icons (for Light BG)
                // isAppearanceLightNavigationBars = false -> White Icons (for Dark BG)
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }

            IvanAppTheme(darkTheme = isDark) {
                // CRITICAL REQ 2: Keyboard Management (adjustResize handled in Manifest + imePadding here)
                IvanAppContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IvanAppContent() {
    var text by remember { mutableStateOf("") }
    val backgroundColor = MaterialTheme.colorScheme.background

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .imePadding(), // Pushes content up when keyboard opens
        topBar = {
            TopAppBar(
                title = { Text("Ivan Model Pro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Mock content area
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(10) { index ->
                    Text(
                        text = "Conversation Item $index",
                        modifier = Modifier.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Input area that stays above keyboard
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ask Ivan anything...") },
                    singleLine = true
                )
            }
        }
    }
}