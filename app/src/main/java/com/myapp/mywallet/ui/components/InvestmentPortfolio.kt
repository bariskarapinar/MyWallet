package com.myapp.mywallet.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InvestmentPortfolio(
    modifier: Modifier = Modifier
) {
    val investments = listOf(
        InvestmentItem("Apple Inc.", "AAPL", "+2.4%", "$189.40", Color(0xFF81C784)),
        InvestmentItem("Bitcoin", "BTC", "+5.1%", "$64,250", Color(0xFF81C784)),
        InvestmentItem("Tesla", "TSLA", "-1.2%", "$175.20", Color(0xFFE57373)),
        InvestmentItem("Ethereum", "ETH", "+0.8%", "$3,420", Color(0xFF81C784))
    )

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Investment Portfolio",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(investments) { item ->
                InvestmentCard(item)
            }
        }
    }
}

data class InvestmentItem(val name: String, val symbol: String, val change: String, val price: String, val color: Color)

@Composable
fun InvestmentCard(item: InvestmentItem) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.symbol, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = item.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = item.price, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = item.change, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = item.color)
        }
    }
}
