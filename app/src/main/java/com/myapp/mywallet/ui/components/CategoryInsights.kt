package com.myapp.mywallet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.mywallet.data.local.entity.ExpenseEntity

@Composable
fun CategoryInsights(
    expenses: List<ExpenseEntity>,
    modifier: Modifier = Modifier
) {
    val categoryTotals = expenses.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
    
    val totalAmount = categoryTotals.values.sum().coerceAtLeast(1.0)
    val colors = listOf(
        Color(0xFF64B5F6), Color(0xFF81C784), Color(0xFFFFD54F), 
        Color(0xFFE57373), Color(0xFFBA68C8), Color(0xFF4DB6AC)
    )

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Spending by Category",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(120.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    var startAngle = -90f
                    categoryTotals.values.forEachIndexed { index, amount ->
                        val sweepAngle = (amount / totalAmount * 360f).toFloat()
                        drawArc(
                            color = colors[index % colors.size],
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = true
                        )
                        startAngle += sweepAngle
                    }
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            Column {
                categoryTotals.entries.take(4).forEachIndexed { index, entry ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Canvas(modifier = Modifier.size(10.dp)) {
                            drawCircle(color = colors[index % colors.size])
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${entry.key}: $${String.format(java.util.Locale.US, "%.0f", entry.value)}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
