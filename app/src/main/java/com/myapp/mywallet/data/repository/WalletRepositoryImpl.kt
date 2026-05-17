package com.myapp.mywallet.data.repository

import com.myapp.mywallet.data.local.dao.CardDao
import com.myapp.mywallet.data.local.dao.ExpenseDao
import com.myapp.mywallet.data.local.entity.CardEntity
import com.myapp.mywallet.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalletRepositoryImpl @Inject constructor(
    private val cardDao: CardDao,
    private val expenseDao: ExpenseDao
) : WalletRepository {
    override fun getAllCards(): Flow<List<CardEntity>> = cardDao.getAllCards()

    override suspend fun getCardById(cardId: Long): CardEntity? = cardDao.getCardById(cardId)

    override suspend fun insertCard(card: CardEntity): Long = cardDao.insertCard(card)

    override suspend fun deleteCard(card: CardEntity) = cardDao.deleteCard(card)

    override suspend fun updateCard(card: CardEntity) = cardDao.updateCard(card)

    override fun getExpensesForCard(cardId: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesForCard(cardId)

    override suspend fun insertExpense(expense: ExpenseEntity) = expenseDao.insertExpense(expense)

    override suspend fun deleteExpense(expense: ExpenseEntity) = expenseDao.deleteExpense(expense)
}
