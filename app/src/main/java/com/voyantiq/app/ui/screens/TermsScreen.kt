package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TermsScreen(
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SmallTopAppBar(
            title = { Text("Terms of Service") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Text("←")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Last Updated: January 11, 2024",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            TermsSection(
                title = "1. Introduction",
                content = "Welcome to Voyant IQ. By using our services, you agree to these Terms of Service. If you do not agree, please do not use our app or services."
            )

            TermsSection(
                title = "2. Account Registration",
                content = """
                    • You must provide accurate information during registration
                    • You are responsible for maintaining account security
                    • You must be at least 18 years old to use our services
                """.trimIndent()
            )

            TermsSection(
                title = "3. Use of Services",
                content = """
                    • Our services are for personal and business travel planning
                    • You agree not to misuse or abuse our services
                    • We reserve the right to suspend accounts that violate our terms
                """.trimIndent()
            )

            // Add more sections as needed
        }
    }
}

@Composable
private fun TermsSection(
    title: String,
    content: String
) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )

    Text(
        text = content,
        fontSize = 16.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}