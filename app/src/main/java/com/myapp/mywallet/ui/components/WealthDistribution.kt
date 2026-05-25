package com.myapp.mywallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WealthDistribution(
    modifier: Modifier = Modifier
) {
    val items = listOf(
        DistributionItem("Cash", 0.4f, Color(0xFF6C63FF)),
        DistributionItem("Stocks", 0.35f, Color(0xFFFF6584)),
        DistributionItem("Crypto", 0.15f, Color(0xFF00BFA5)),
        DistributionItem("Gold", 0.1f, Color(0xFFFFD54F))
    )

    Card(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Asset Allocation",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3436)
            )
            
            Spacer(modifier = Modifier.height(20.dp))

            // Stacked Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(item.percentage)
                            .background(item.color)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                items.forEach { item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(8.dp),
                            shape = RoundedCornerShape(2.dp),
                            color = item.color
                        ) {}
                        Text(text = item.label, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                        Text(text = "${(item.percentage * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

data class DistributionItem(val label: String, val percentage: Float, val color: Color)
