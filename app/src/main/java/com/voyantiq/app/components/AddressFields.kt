package com.voyantiq.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mapbox.search.*
import com.mapbox.search.result.SearchResult
import kotlinx.coroutines.launch

data class Address(
    val streetAddress: String,
    val aptSuite: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Composable
fun AddressFields(
    address: Address,
    onAddressChange: (Address) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showSuggestions by remember { mutableStateOf(false) }
    var suggestions by remember { mutableStateOf<List<SearchResult>>(emptyList()) }

    // Initialize Mapbox Search
    val searchEngine = remember {
        SearchEngine.createSearchEngine(
            SearchEngineSettings(
                context.getString(R.string.mapbox_access_token)
            )
        )
    }

    val searchCallback = remember {
        object : SearchCallback {
            override fun onResults(results: List<SearchResult>, responseInfo: ResponseInfo) {
                suggestions = results
                showSuggestions = results.isNotEmpty()
            }

            override fun onError(e: Exception) {
                // Handle error
            }
        }
    }

    Column(modifier = modifier) {
        // Street Address with Autofill
        OutlinedTextField(
            value = address.streetAddress,
            onValueChange = { input ->
                onAddressChange(address.copy(streetAddress = input))
                if (input.length >= 3) {
                    scope.launch {
                        searchEngine.search(
                            input,
                            SearchOptions(
                                limit = 5,
                                types = listOf(
                                    QueryType.ADDRESS,
                                    QueryType.POI,
                                    QueryType.STREET
                                )
                            ),
                            searchCallback
                        )
                    }
                } else {
                    showSuggestions = false
                }
            },
            label = { Text("Street Address") },
            placeholder = { Text("Start typing for suggestions") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Suggestions dropdown
        if (showSuggestions) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                shadowElevation = 4.dp
            ) {
                LazyColumn {
                    items(suggestions) { suggestion ->
                        ListItem(
                            headlineContent = { Text(suggestion.name) },
                            supportingContent = { Text(suggestion.address?.formattedAddress() ?: "") },
                            modifier = Modifier.clickable {
                                suggestion.address?.let { addr ->
                                    onAddressChange(
                                        Address(
                                            streetAddress = "${addr.houseNumber ?: ""} ${addr.street ?: ""}".trim(),
                                            aptSuite = "",
                                            city = addr.place ?: "",
                                            state = addr.region ?: "",
                                            zipCode = addr.postcode ?: "",
                                            latitude = suggestion.coordinate.latitude(),
                                            longitude = suggestion.coordinate.longitude()
                                        )
                                    )
                                }
                                showSuggestions = false
                            }
                        )
                    }
                }
            }
        }

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
                singleLine = true,
                enabled = false  // Disabled because it's filled by autofill
            )

            OutlinedTextField(
                value = address.state,
                onValueChange = { onAddressChange(address.copy(state = it)) },
                label = { Text("State") },
                placeholder = { Text("State") },
                modifier = Modifier.width(100.dp),
                singleLine = true,
                enabled = false  // Disabled because it's filled by autofill
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
            singleLine = true,
            enabled = false  // Disabled because it's filled by autofill
        )
    }
}

// Helper function to format address
private fun com.mapbox.search.result.Address.formattedAddress(): String {
    return buildString {
        if (!houseNumber.isNullOrEmpty()) append("$houseNumber ")
        if (!street.isNullOrEmpty()) append("$street, ")
        if (!place.isNullOrEmpty()) append("$place, ")
        if (!region.isNullOrEmpty()) append("$region ")
        if (!postcode.isNullOrEmpty()) append(postcode)
    }.trim()
}

// Validation function remains the same
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