package com.myapp.mywallet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import com.myapp.mywallet.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myapp.mywallet.data.local.entity.CardEntity
import com.myapp.mywallet.model.CardType
import com.myapp.mywallet.ui.theme.*

@Composable
fun CreditCard(
    card: CardEntity,
    modifier: Modifier = Modifier
) {
    val gradientColors = getGradientColors(card.cardType)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(gradientColors))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val logo = getCardLogo(card.cardType)
                if (logo != null) {
                    Icon(
                        painter = painterResource(id = logo),
                        contentDescription = card.cardType.name,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Unspecified
                    )
                } else {
                    Text(
                        text = card.cardType.name,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "$${String.format(java.util.Locale.US, "%.2f", card.balance)}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = formatCardNumber(card.cardNumber),
                color = Color.White,
                fontSize = 24.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "CARD HOLDER",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                    Text(
                        text = card.cardHolderName.uppercase(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "EXPIRES",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                    Text(
                        text = card.expiryDate,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun getCardLogo(cardType: CardType): Int? {
    return when (cardType) {
        CardType.VISA -> R.drawable.ic_visa
        CardType.MASTERCARD -> R.drawable.ic_mastercard
        CardType.AMEX -> R.drawable.ic_amex
        else -> null
    }
}

private fun getGradientColors(cardType: CardType): List<Color> {
    return when (cardType) {
        CardType.VISA -> listOf(VisaStart, VisaEnd)
        CardType.MASTERCARD -> listOf(MastercardStart, MastercardEnd)
        CardType.AMEX -> listOf(AmexStart, AmexEnd)
        CardType.CHINAUNIONPAY -> listOf(UnionPayStart, UnionPayEnd)
        CardType.EUROUPAY -> listOf(Purple40, Purple80) // Placeholder
        CardType.DISCOVER -> listOf(DiscoverStart, DiscoverEnd)
    }
}

private fun formatCardNumber(number: String): String {
    return "**** **** **** ${number.takeLast(4)}"
}
