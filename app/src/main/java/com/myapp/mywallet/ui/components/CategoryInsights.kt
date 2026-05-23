package com.myapp.mywallet.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myapp.mywallet.data.local.entity.ExpenseEntity

@Composable
fun CategoryInsights(
    expenses: List<ExpenseEntity>,
    modifier: Modifier = Modifier
) {
    var showDetails by remember { mutableStateOf(false) }
    val categoryTotals = expenses.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
    
    val totalAmount = categoryTotals.values.sum().coerceAtLeast(1.0)
    val colors = listOf(
        Color(0xFF6C63FF), Color(0xFFFF6584), Color(0xFF00BFA5), 
        Color(0xFFFFD54F), Color(0xFFBA68C8), Color(0xFF4DB6AC)
    )

    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(expenses) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(1f, tween(1200, easing = FastOutSlowInEasing))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { showDetails = true },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Spending Breakdown",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3436)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.fillMaxSize().rotate(-90f)) {
                        var startAngle = 0f
                        categoryTotals.values.forEachIndexed { index, amount ->
                            val sweepAngle = (amount / totalAmount * 360f).toFloat() * animatedProgress.value
                            drawArc(
                                color = colors[index % colors.size],
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = 20.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
                            )
                            startAngle += (amount / totalAmount * 360f).toFloat()
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Total",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "$${String.format(java.util.Locale.US, "%.0f", totalAmount)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF2D3436)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(32.dp))

                Column {
                    categoryTotals.entries.take(4).forEachIndexed { index, entry ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(12.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = colors[index % colors.size]
                            ) {}
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = entry.key,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2D3436)
                                )
                                Text(
                                    text = "$${String.format(java.util.Locale.US, "%.0f", entry.value)}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
            
            Text(
                text = "Tap for category details",
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
            )
        }
    }

    if (showDetails) {
        CategoryDetail(categoryTotals = categoryTotals, colors = colors, totalAmount = totalAmount, onDismiss = { showDetails = false })
    }
}

@Composable
fun CategoryDetail(categoryTotals: Map<String, Double>, colors: List<Color>, totalAmount: Double, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.7f)
                    .padding(16.dp)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Category Analysis",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(categoryTotals.entries.toList()) { (category, amount) ->
                            val percentage = (amount / totalAmount * 100).toInt()
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            modifier = Modifier.size(16.dp),
                                            shape = RoundedCornerShape(4.dp),
                                            color = colors[categoryTotals.keys.toList().indexOf(category) % colors.size]
                                        ) {}
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(text = category, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    }
                                    Text(text = "$${String.format(java.util.Locale.US, "%.2f", amount)}", fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { amount.toFloat() / totalAmount.toFloat() },
                                    modifier = Modifier.fillMaxWidth().height(8.dp),
                                    color = colors[categoryTotals.keys.toList().indexOf(category) % colors.size],
                                    trackColor = Color.LightGray.copy(alpha = 0.2f),
                                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                                )
                                Text(
                                    text = "$percentage% of total spending",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
                    ) {
                        Text("Close Analysis", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
