package com.myapp.mywallet.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myapp.mywallet.data.local.entity.ExpenseEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SpendingChart(
    expenses: List<ExpenseEntity>,
    modifier: Modifier = Modifier
) {
    var showDetails by remember { mutableStateOf(false) }
    val animationProgress = remember { Animatable(0f) }
    
    LaunchedEffect(expenses) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, tween(1000))
    }

    val maxAmount = expenses.maxOfOrNull { it.amount }?.coerceAtLeast(100.0) ?: 100.0
    val chartColor = Color(0xFF6C63FF) // Primary Colorful

    Column(
        modifier = modifier
            .padding(16.dp)
            .clickable { showDetails = true }
    ) {
        Text(
            text = "Spending Trend",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(horizontal = 8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height
                val spacing = width / (expenses.size.coerceAtLeast(2) - 1).coerceAtLeast(1)
                
                if (expenses.size > 1) {
                    val path = Path()
                    val fillPath = Path()
                    
                    expenses.forEachIndexed { index, expense ->
                        val x = index * spacing
                        val y = height - (expense.amount / maxAmount * height).toFloat() * animationProgress.value
                        
                        if (index == 0) {
                            path.moveTo(x, y)
                            fillPath.moveTo(x, height)
                            fillPath.lineTo(x, y)
                        } else {
                            path.lineTo(x, y)
                            fillPath.lineTo(x, y)
                        }
                        
                        if (index == expenses.size - 1) {
                            fillPath.lineTo(x, height)
                            fillPath.close()
                        }
                    }
                    
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(chartColor.copy(alpha = 0.4f), Color.Transparent)
                        )
                    )
                    
                    drawPath(
                        path = path,
                        color = chartColor,
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
            }
        }
        
        Text(
            text = "Tap to see full history",
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp)
        )
    }

    if (showDetails) {
        SpendingTrendDetail(expenses = expenses, onDismiss = { showDetails = false })
    }
}

@Composable
fun SpendingTrendDetail(expenses: List<ExpenseEntity>, onDismiss: () -> Unit) {
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
                    .fillMaxHeight(0.8f)
                    .padding(16.dp)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Full Spending History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(expenses.reversed()) { expense ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = expense.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(
                                        text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(expense.date)),
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    text = "-$${String.format(Locale.US, "%.2f", expense.amount)}",
                                    color = Color(0xFFE57373),
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = Color.LightGray.copy(alpha = 0.3f))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
                    ) {
                        Text("Close History", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
