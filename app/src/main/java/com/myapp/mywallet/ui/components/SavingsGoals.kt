package com.myapp.mywallet.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SavingsGoals(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Savings Goals",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        GoalItem(name = "New MacBook Pro", current = 1200f, target = 2500f, color = Color(0xFF64B5F6))
        Spacer(modifier = Modifier.height(12.dp))
        GoalItem(name = "Summer Vacation", current = 800f, target = 1500f, color = Color(0xFF81C784))
        Spacer(modifier = Modifier.height(12.dp))
        GoalItem(name = "Emergency Fund", current = 4500f, target = 5000f, color = Color(0xFFFFD54F))
    }
}

@Composable
fun GoalItem(name: String, current: Float, target: Float, color: Color) {
    val progress = current / target
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(text = "$${current.toInt()} / $${target.toInt()}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = color,
                trackColor = color.copy(alpha = 0.1f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }
    }
}
