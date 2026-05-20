package com.myapp.mywallet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.mywallet.data.local.entity.CardEntity
import com.myapp.mywallet.data.local.entity.ExpenseEntity
import com.myapp.mywallet.data.repository.WalletRepository
import com.myapp.mywallet.model.CardType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    private val _cards = MutableStateFlow<List<CardEntity>>(emptyList())
    val cards: StateFlow<List<CardEntity>> = _cards.asStateFlow()

    private val _selectedCard = MutableStateFlow<CardEntity?>(null)
    val selectedCard: StateFlow<CardEntity?> = _selectedCard.asStateFlow()

    private val _expenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val expenses: StateFlow<List<ExpenseEntity>> = _expenses.asStateFlow()

    init {
        loadCards()
    }

    private val _isAuthSuccessful = MutableStateFlow(false)
    val isAuthSuccessful: StateFlow<Boolean> = _isAuthSuccessful.asStateFlow()

    fun onAuthSuccess() {
        _isAuthSuccessful.value = true
    }

    private fun loadCards() {
        viewModelScope.launch {
            repository.getAllCards().collect { cardList ->
                if (cardList.isEmpty()) {
                    addInitialData()
                } else {
                    _cards.value = cardList
                    if (_selectedCard.value == null) {
                        selectCard(cardList[0])
                    }
                }
            }
        }
    }

    private suspend fun addInitialData() {
        val initialCards = listOf(
            CardEntity(
                cardHolderName = "John Doe",
                cardNumber = "1234567890123456",
                cardType = CardType.VISA,
                expiryDate = "12/28",
                balance = 5420.50,
                backgroundColor = "#1A237E",
                gradientColors = "#1A237E,#536DFE",
                currency = "USD",
                rewardPoints = 1250
            ),
            CardEntity(
                cardHolderName = "John Doe",
                cardNumber = "9876543210987654",
                cardType = CardType.MASTERCARD,
                expiryDate = "05/26",
                balance = 2150.75,
                backgroundColor = "#C62828",
                gradientColors = "#C62828,#FF8F00",
                currency = "EUR",
                rewardPoints = 840
            ),
            CardEntity(
                cardHolderName = "John Doe",
                cardNumber = "5555444433332222",
                cardType = CardType.AMEX,
                expiryDate = "09/27",
                balance = 8900.00,
                backgroundColor = "#004D40",
                gradientColors = "#004D40,#00BFA5",
                currency = "GBP",
                rewardPoints = 3200
            ),
            CardEntity(
                cardHolderName = "John Doe",
                cardNumber = "6212345678901234",
                cardType = CardType.CHINAUNIONPAY,
                expiryDate = "11/29",
                balance = 15400.00,
                backgroundColor = "#B71C1C",
                gradientColors = "#B71C1C,#0D47A1",
                currency = "USD",
                rewardPoints = 540
            ),
            CardEntity(
                cardHolderName = "John Doe",
                cardNumber = "6011000011112222",
                cardType = CardType.DISCOVER,
                expiryDate = "03/27",
                balance = 3200.00,
                backgroundColor = "#E65100",
                gradientColors = "#E65100,#F9A825",
                currency = "USD",
                rewardPoints = 120
            )
        )

        initialCards.forEach { card ->
            val cardId = repository.insertCard(card)
            addInitialExpenses(cardId)
        }
    }

    private suspend fun addInitialExpenses(cardId: Long) {
        val expenses = listOf(
            ExpenseEntity(cardId = cardId, title = "Apple Store", amount = 129.0, date = System.currentTimeMillis() - 86400000, category = "Tech"),
            ExpenseEntity(cardId = cardId, title = "Starbucks", amount = 15.50, date = System.currentTimeMillis() - 172800000, category = "Food"),
            ExpenseEntity(cardId = cardId, title = "Uber", amount = 24.0, date = System.currentTimeMillis() - 259200000, category = "Transport"),
            ExpenseEntity(cardId = cardId, title = "Netflix", amount = 15.99, date = System.currentTimeMillis() - 345600000, category = "Entertainment"),
            ExpenseEntity(cardId = cardId, title = "Amazon", amount = 85.00, date = System.currentTimeMillis() - 432000000, category = "Shopping"),
            ExpenseEntity(cardId = cardId, title = "Gym Membership", amount = 50.00, date = System.currentTimeMillis() - 518400000, category = "Health"),
            ExpenseEntity(cardId = cardId, title = "Gas Station", amount = 45.20, date = System.currentTimeMillis() - 604800000, category = "Transport"),
            ExpenseEntity(cardId = cardId, title = "Grocery Mart", amount = 120.45, date = System.currentTimeMillis() - 691200000, category = "Food"),
            ExpenseEntity(cardId = cardId, title = "Pharmacy", amount = 12.30, date = System.currentTimeMillis() - 777600000, category = "Health"),
            ExpenseEntity(cardId = cardId, title = "Local Cafe", amount = 8.50, date = System.currentTimeMillis() - 864000000, category = "Food"),
            ExpenseEntity(cardId = cardId, title = "Zara", amount = 150.00, date = System.currentTimeMillis() - 950400000, category = "Shopping"),
            ExpenseEntity(cardId = cardId, title = "Delta Airlines", amount = 450.00, date = System.currentTimeMillis() - 1036800000, category = "Travel"),
            ExpenseEntity(cardId = cardId, title = "Hilton Hotels", amount = 230.00, date = System.currentTimeMillis() - 1123200000, category = "Travel"),
            ExpenseEntity(cardId = cardId, title = "Steam Games", amount = 59.99, date = System.currentTimeMillis() - 1209600000, category = "Entertainment"),
            ExpenseEntity(cardId = cardId, title = "Adobe CC", amount = 52.99, date = System.currentTimeMillis() - 1296000000, category = "Tech")
        )
        expenses.forEach { repository.insertExpense(it) }
    }

    fun selectCard(card: CardEntity) {
        _selectedCard.value = card
        viewModelScope.launch {
            repository.getExpensesForCard(card.id).collect { expenseList ->
                _expenses.value = expenseList
            }
        }
    }

    fun addDummyCard() {
        viewModelScope.launch {
            val dummyCard = CardEntity(
                cardHolderName = "John Doe",
                cardNumber = "1234567890123456",
                cardType = CardType.entries.random(),
                expiryDate = "12/28",
                balance = 5000.0,
                backgroundColor = "#000000",
                gradientColors = "#000000,#FFFFFF"
            )
            repository.insertCard(dummyCard)
        }
    }

    fun addDummyExpense(cardId: Long) {
        viewModelScope.launch {
            val dummyExpense = ExpenseEntity(
                cardId = cardId,
                title = "Coffee Shop",
                amount = 4.50,
                date = System.currentTimeMillis(),
                category = "Food"
            )
            repository.insertExpense(dummyExpense)
        }
    }
}
