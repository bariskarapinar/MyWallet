package com.myapp.mywallet.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.mywallet.model.CardType

@Composable
fun CardPerks(
    cardType: CardType,
    modifier: Modifier = Modifier
) {
    val perks = when (cardType) {
        CardType.AMEX -> listOf("Airport Lounge Access", "Travel Insurance", "Concierge 24/7")
        CardType.VISA -> listOf("5% Cashback Food", "Extended Warranty", "Roadside Assist")
        CardType.MASTERCARD -> listOf("Free Streaming", "Dining Discounts", "Fraud Protect")
        else -> listOf("Cashback Deals", "Zero Liability", "Secure Pay")
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Exclusive Perks",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2D3436),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(perks) { perk ->
                PerkItem(perk)
            }
        }
    }
}

@Composable
fun PerkItem(perk: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF6C63FF).copy(alpha = 0.1f),
        modifier = Modifier.height(48.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.LocalOffer, contentDescription = null, tint = Color(0xFF6C63FF), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = perk, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF6C63FF))
        }
    }
}
