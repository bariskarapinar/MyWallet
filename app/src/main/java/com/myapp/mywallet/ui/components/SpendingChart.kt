package com.myapp.mywallet.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.mywallet.data.local.entity.ExpenseEntity

@Composable
fun SpendingChart(
    expenses: List<ExpenseEntity>,
    modifier: Modifier = Modifier
) {
    val animationProgress = remember { Animatable(0f) }
    
    LaunchedEffect(expenses) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, tween(1000))
    }

    val maxAmount = expenses.maxOfOrNull { it.amount }?.coerceAtLeast(100.0) ?: 100.0
    val chartColor = MaterialTheme.colorScheme.primary

    Column(modifier = modifier.padding(16.dp)) {
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
                            colors = listOf(chartColor.copy(alpha = 0.3f), Color.Transparent),
                            startY = 0f,
                            endY = height
                        )
                    )
                    
                    drawPath(
                        path = path,
                        color = chartColor,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
        }
    }
}
