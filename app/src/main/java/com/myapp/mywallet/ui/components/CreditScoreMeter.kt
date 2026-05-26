package com.myapp.mywallet.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CreditScoreMeter(
    modifier: Modifier = Modifier,
    score: Int = 750,
) {
    var showDetails by remember { mutableStateOf(value = false) }
    val animationProgress = remember { Animatable(initialValue = 0f) }
    
    LaunchedEffect(key1 = score) {
        animationProgress.animateTo(targetValue = score / 850f, animationSpec = tween(durationMillis = 1500))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { showDetails = true },
        shape = RoundedCornerShape(size = 24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(all = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Credit Score",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(height = 16.dp))

            Box(modifier = Modifier.size(size = 200.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 12.dp.toPx()
                    val centerOffset = Offset(x = size.width / 2, y = size.height / 2)
                    val radius = (size.minDimension / 2) - strokeWidth
                    
                    // Background Arc
                    drawArc(
                        color = Color.Gray.copy(alpha = 0.1f),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    
                    // Score Gradient Arc
                    drawArc(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFE57373), Color(0xFFFFD54F), Color(0xFF81C784))
                        ),
                        startAngle = 180f,
                        sweepAngle = 180f * animationProgress.value,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )

                    // Pointer (Needle)
                    val angleInRadians = (180f + (180f * animationProgress.value)) * (Math.PI / 180f).toFloat()
                    val needleLength = radius - 10.dp.toPx()
                    val needleEnd = Offset(
                        x = centerOffset.x + (needleLength * cos(angleInRadians)),
                        y = centerOffset.y + (needleLength * sin(angleInRadians))
                    )
                    
                    val pointerColor = Color.White

                    drawLine(
                        color = pointerColor,
                        start = centerOffset,
                        end = needleEnd,
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                    
                    drawCircle(
                        color = pointerColor,
                        radius = 8.dp.toPx(),
                        center = centerOffset
                    )
                }
                
                Column(
                    modifier = Modifier.padding(top = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (animationProgress.value * 850).toInt().toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "EXCELLENT",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF81C784)
                    )
                }
            }
            
            Text(
                text = "Tap for more details",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }

    if (showDetails) {
        CreditScoreDetail(
            score = score,
            onDismiss = { showDetails = false }
        )
    }
}

@Composable
fun CreditScoreDetail(score: Int, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.9f)
                    .padding(all = 16.dp)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(size = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(all = 24.dp)) {
                    Text(
                        text = "Credit Analysis",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436)
                    )
                    Spacer(modifier = Modifier.height(height = 16.dp))
                    
                    // Using a static version for the detail view to avoid recursive call loops if needed, 
                    // though here it's fine as it's a dialog. Let's keep it clean.
                    Box(modifier = Modifier.fillMaxWidth().height(height = 150.dp), contentAlignment = Alignment.Center) {
                         Text(text = "Detailed Chart Analysis", color = Color.Gray, fontSize = 14.sp)
                    }

                    Text(
                        text = "Your score of $score is in the top tier. Keep maintaining your timely payments!",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(height = 24.dp))
                    
                    Text(
                        text = "Impact Factors",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3436)
                    )
                    
                    Spacer(modifier = Modifier.height(height = 12.dp))
                    
                    FactorItem(label = "Payment History", status = "Excellent", color = Color(0xFF81C784))
                    FactorItem(label = "Credit Utilization", status = "3% - Ideal", color = Color(0xFF81C784))
                    FactorItem(label = "Hard Inquiries", status = "None", color = Color(0xFF81C784))

                    Spacer(modifier = Modifier.height(height = 24.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(size = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C63FF))
                    ) {
                        Text(text = "Close Analysis", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun FactorItem(label: String, status: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 14.sp, color = Color.DarkGray)
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = color.copy(alpha = 0.1f)
        ) {
            Text(
                text = status,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}
