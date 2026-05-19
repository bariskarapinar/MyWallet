package com.myapp.mywallet.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExchangeRates(
    modifier: Modifier = Modifier
) {
    val rates = listOf(
        "EUR/USD" to "1.0845",
        "GBP/USD" to "1.2630",
        "USD/JPY" to "151.40",
        "BTC/USD" to "64,250"
    )

    Card(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            rates.forEach { (pair, rate) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = pair, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Text(text = rate, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
