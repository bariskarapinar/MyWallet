package com.myapp.mywallet.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SpendingLimitGauge(
    current: Double,
    limit: Double,
    modifier: Modifier = Modifier
) {
    val targetProgress = (current / limit).toFloat().coerceIn(0f, 1f)
    val animatedProgress = remember { Animatable(0f) }
    
    LaunchedEffect(current, limit) {
        animatedProgress.animateTo(targetProgress, tween(1000, easing = FastOutSlowInEasing))
    }

    val color = when {
        animatedProgress.value > 0.9f -> Color(0xFFFF6584)
        animatedProgress.value > 0.7f -> Color(0xFFFFD54F)
        else -> Color(0xFF00BFA5)
    }

    Card(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color.LightGray.copy(alpha = 0.1f),
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(Color(0xFF81C784), Color(0xFFFFD54F), Color(0xFFFF6584))
                        ),
                        startAngle = 135f,
                        sweepAngle = 270f * animatedProgress.value,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(animatedProgress.value * 100).toInt()}%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2D3436)
                    )
                    Text(
                        text = "USED",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column {
                Text(
                    text = "Monthly Limit",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3436)
                )
                Text(
                    text = "$${String.format(java.util.Locale.US, "%,.0f", current)} of $${String.format(java.util.Locale.US, "%,.0f", limit)}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = if (animatedProgress.value > 0.9f) "CRITICAL" else "HEALTHY",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }
        }
    }
}
