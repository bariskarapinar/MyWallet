package com.myapp.mywallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoyaltyCards(
    modifier: Modifier = Modifier
) {
    val storeCards = listOf(
        LoyaltyCardItem("Starbucks", "Gold Member", Color(0xFF00704A), Color(0xFF1E3932)),
        LoyaltyCardItem("IKEA", "Family Card", Color(0xFF0058AB), Color(0xFF003366)),
        LoyaltyCardItem("H&M", "Member", Color(0xFFCC0000), Color(0xFF990000)),
        LoyaltyCardItem("Apple", "Specialist", Color(0xFF555555), Color(0xFF000000))
    )

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Store Loyalty Cards",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3436),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(storeCards) { card ->
                StoreCard(card)
            }
        }
    }
}

data class LoyaltyCardItem(val name: String, val level: String, val startColor: Color, val endColor: Color)

@Composable
fun StoreCard(card: LoyaltyCardItem) {
    Card(
        modifier = Modifier.width(140.dp).height(90.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(listOf(card.startColor, card.endColor)))
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = card.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Default.QrCode, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
                Text(text = card.level, color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
            }
        }
    }
}
