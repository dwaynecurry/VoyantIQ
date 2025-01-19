package com.voyantiq.app.ui.dialogs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.voyantiq.app.data.model.Activity
import com.voyantiq.app.data.model.ActivityType

@Composable
fun BudgetBreakdownDialog(
    onDismiss: () -> Unit,
    activities: List<Activity>
) {
    val totalBudget = activities.sumOf { it.cost }
    val typeBreakdown = activities.groupBy { it.type }
        .mapValues { it.value.sumOf { activity -> activity.cost } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Budget Breakdown") },
        text = {
            Column {
                // Pie Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                ) {
                    BudgetPieChart(
                        typeBreakdown = typeBreakdown,
                        totalBudget = totalBudget
                    )
                }

                // Budget Summary
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Total Budget: $${String.format("%.2f", totalBudget)}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Activities: ${activities.size}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "Average per Activity: $${String.format("%.2f", totalBudget / activities.size)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Breakdown by Type
                LazyColumn {
                    items(typeBreakdown.entries.toList()) { (type, cost) ->
                        BudgetTypeRow(
                            type = type,
                            cost = cost,
                            totalBudget = totalBudget,
                            count = activities.count { it.type == type }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun BudgetPieChart(
    typeBreakdown: Map<ActivityType, Double>,
    totalBudget: Double
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = minOf(size.width, size.height) / 2
        var startAngle = 0f

        typeBreakdown.forEach { (type, cost) ->
            val sweepAngle = (cost / totalBudget * 360).toFloat()
            drawArc(
                color = getTypeColor(type),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )
            // Draw outline
            drawArc(
                color = Color.White,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                style = Stroke(width = 2f),
                size = Size(radius * 2, radius * 2),
                topLeft = Offset(center.x - radius, center.y - radius)
            )
            startAngle += sweepAngle
        }
    }
}

@Composable
private fun BudgetTypeRow(
    type: ActivityType,
    cost: Double,
    totalBudget: Double,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = getTypeIcon(type),
                contentDescription = null,
                tint = getTypeColor(type)
            )
            Column {
                Text(
                    text = type.name.replace("_", " "),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "$count activities",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$${String.format("%.2f", cost)}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${String.format("%.1f", cost / totalBudget * 100)}%",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

private fun getTypeColor(type: ActivityType): Color = when(type) {
    ActivityType.DINING -> Color(0xFF4CAF50)
    ActivityType.SIGHTSEEING -> Color(0xFF2196F3)
    ActivityType.ENTERTAINMENT -> Color(0xFFFFC107)
    ActivityType.TRANSPORTATION -> Color(0xFF9C27B0)
    ActivityType.ACCOMMODATION -> Color(0xFF795548)
}

private fun getTypeIcon(type: ActivityType) = when(type) {
    ActivityType.DINING -> Icons.Default.Restaurant
    ActivityType.SIGHTSEEING -> Icons.Default.Landscape
    ActivityType.ENTERTAINMENT -> Icons.Default.TheaterComedy
    ActivityType.TRANSPORTATION -> Icons.Default.DirectionsCar
    ActivityType.ACCOMMODATION -> Icons.Default.Hotel
}