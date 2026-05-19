package com.myapp.mywallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
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
fun UpcomingBills(
    modifier: Modifier = Modifier
) {
    val bills = listOf(
        BillItem("Netflix", "Monthly Premium", 15.99, "May 24"),
        BillItem("Spotify", "Family Plan", 10.99, "May 26"),
        BillItem("AWS", "Cloud Services", 45.50, "May 29")
    )

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Upcoming Bills",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        bills.forEach { bill ->
            BillCard(bill)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

data class BillItem(val title: String, val desc: String, val amount: Double, val date: String)

@Composable
fun BillCard(bill: BillItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = bill.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = bill.desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "$${bill.amount}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE57373))
                Text(text = bill.date, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}
