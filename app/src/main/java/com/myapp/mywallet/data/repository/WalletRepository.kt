package com.myapp.mywallet.data.repository

import com.myapp.mywallet.data.local.entity.CardEntity
import com.myapp.mywallet.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun getAllCards(): Flow<List<CardEntity>>
    suspend fun getCardById(cardId: Long): CardEntity?
    suspend fun insertCard(card: CardEntity): Long
    suspend fun deleteCard(card: CardEntity)
    suspend fun updateCard(card: CardEntity)

    fun getExpensesForCard(cardId: Long): Flow<List<ExpenseEntity>>
    suspend fun insertExpense(expense: ExpenseEntity)
    suspend fun deleteExpense(expense: ExpenseEntity)
}
