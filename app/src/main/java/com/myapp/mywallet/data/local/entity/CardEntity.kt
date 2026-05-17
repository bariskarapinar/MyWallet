package com.myapp.mywallet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myapp.mywallet.model.CardType

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cardHolderName: String,
    val cardNumber: String,
    val cardType: CardType,
    val expiryDate: String,
    val balance: Double,
    val backgroundColor: String, // Hex color
    val gradientColors: String // Comma separated hex colors
)
