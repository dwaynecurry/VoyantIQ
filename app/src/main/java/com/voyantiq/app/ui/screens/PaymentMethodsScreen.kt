package com.voyantiq.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PaymentMethodsScreen(
    onBackClick: () -> Unit,
    onAddPaymentMethod: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBackClick) {
                Text("←")
            }
            Text(
                text = "Payment Methods",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Button(
            onClick = onAddPaymentMethod,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Payment Method")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(samplePaymentMethods) { method ->
                PaymentMethodItem(method)
            }
        }
    }
}

@Composable
private fun PaymentMethodItem(
    paymentMethod: PaymentMethodData
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = paymentMethod.type,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = paymentMethod.lastFourDigits,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { /* Edit payment method */ }) {
                Text("✏️")
            }
        }
    }
}

private data class PaymentMethodData(
    val type: String,
    val lastFourDigits: String
)

private val samplePaymentMethods = listOf(
    PaymentMethodData("Visa", "**** 1234"),
    PaymentMethodData("Mastercard", "**** 5678"),
    PaymentMethodData("PayPal", "user@email.com")
)