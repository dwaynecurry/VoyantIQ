package com.voyantiq.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Address(
    val streetAddress: String,
    val aptSuite: String,
    val city: String,
    val state: String,
    val zipCode: String
)

@Composable
fun AddressFields(
    address: Address,
    onAddressChange: (Address) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = address.streetAddress,
            onValueChange = { onAddressChange(address.copy(streetAddress = it)) },
            label = { Text("Street Address") },
            placeholder = { Text("Enter street address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address.aptSuite,
            onValueChange = { onAddressChange(address.copy(aptSuite = it)) },
            label = { Text("Apt/Suite (Optional)") },
            placeholder = { Text("Enter apartment or suite number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = address.city,
                onValueChange = { onAddressChange(address.copy(city = it)) },
                label = { Text("City") },
                placeholder = { Text("Enter city") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            OutlinedTextField(
                value = address.state,
                onValueChange = { onAddressChange(address.copy(state = it)) },
                label = { Text("State") },
                placeholder = { Text("State") },
                modifier = Modifier.width(100.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address.zipCode,
            onValueChange = {
                if (it.length <= 5 && it.all { char -> char.isDigit() }) {
                    onAddressChange(address.copy(zipCode = it))
                }
            },
            label = { Text("ZIP Code") },
            placeholder = { Text("Enter ZIP code") },
            modifier = Modifier.width(120.dp),
            singleLine = true
        )
    }
}

fun validateAddress(address: Address): Map<String, String?> {
    return mapOf(
        "streetAddress" to when {
            address.streetAddress.isBlank() -> "Street address is required"
            address.streetAddress.length < 5 -> "Please enter a valid street address"
            else -> null
        },
        "city" to when {
            address.city.isBlank() -> "City is required"
            address.city.length < 2 -> "Please enter a valid city name"
            else -> null
        },
        "state" to when {
            address.state.isBlank() -> "State is required"
            address.state.length != 2 -> "Please enter a valid 2-letter state code"
            !address.state.matches(Regex("^[A-Za-z]{2}\$")) -> "Invalid state format"
            else -> null
        },
        "zipCode" to when {
            address.zipCode.isBlank() -> "ZIP code is required"
            address.zipCode.length != 5 -> "ZIP code must be 5 digits"
            !address.zipCode.matches(Regex("^\\d{5}\$")) -> "Invalid ZIP code format"
            else -> null
        }
    )
}