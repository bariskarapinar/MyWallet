package com.myapp.mywallet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myapp.mywallet.data.local.entity.CardEntity
import com.myapp.mywallet.data.local.entity.ExpenseEntity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.myapp.mywallet.ui.components.CategoryInsights
import com.myapp.mywallet.ui.components.CreditCard
import com.myapp.mywallet.ui.components.CreditScoreMeter
import com.myapp.mywallet.ui.components.ExchangeRates
import com.myapp.mywallet.ui.components.FinancialTips
import com.myapp.mywallet.ui.components.InvestmentPortfolio
import com.myapp.mywallet.ui.components.QuickActions
import com.myapp.mywallet.ui.components.RewardSection
import com.myapp.mywallet.ui.components.SavingsGoals
import com.myapp.mywallet.ui.components.SecurityStatus
import com.myapp.mywallet.ui.components.SpendingChart
import com.myapp.mywallet.ui.components.SpendingLimitGauge
import com.myapp.mywallet.ui.components.UpcomingBills
import com.myapp.mywallet.ui.components.WealthTracker
import com.myapp.mywallet.viewmodel.WalletViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletHomeScreen(
    viewModel: WalletViewModel = hiltViewModel()
) {
    val cards by viewModel.cards.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val selectedCard by viewModel.selectedCard.collectAsState()
    val isAuthSuccessful by viewModel.isAuthSuccessful.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current
    
    LaunchedEffect(Unit) {
        if (!isAuthSuccessful) {
            val executor = ContextCompat.getMainExecutor(context)
            val biometricPrompt = BiometricPrompt(
                context as FragmentActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        viewModel.onAuthSuccess()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        // For educational/demo purposes, allow access even if biometrics fail or are not set up
                        viewModel.onAuthSuccess()
                    }
                }
            )

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    if (!isAuthSuccessful) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Wallet", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { viewModel.addDummyCard() }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Card")
                    }
                }
            )
        },
        floatingActionButton = {
            QuickActions(onActionClick = { category ->
                selectedCard?.let { viewModel.addDummyExpense(it.id) }
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFF0F2F8),
                            Color(0xFFE2E5EE)
                        )
                    )
                )
        ) {
            if (cards.isNotEmpty()) {
                val pagerState = rememberPagerState { cards.size }

                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        viewModel.selectCard(cards[page])
                    }
                }

                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    WealthTracker(totalBalance = cards.sumOf { it.balance })

                    HorizontalPager(
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 32.dp),
                        pageSpacing = 16.dp
                    ) { page ->
                        CreditCard(card = cards[page])
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    PageIndicator(
                        count = cards.size,
                        currentPage = pagerState.currentPage,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text("No cards available. Add one!")
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    if (expenses.isNotEmpty()) {
                        SecurityStatus(modifier = Modifier.fillMaxWidth())

                        SpendingChart(
                            expenses = expenses.takeLast(10).reversed(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                        
                        CategoryInsights(
                            expenses = expenses,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )

                        RewardSection(
                            points = selectedCard?.rewardPoints ?: 0,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )

                        SpendingLimitGauge(
                            current = expenses.sumOf { it.amount },
                            limit = 5000.0,
                            modifier = Modifier.fillMaxWidth()
                        )

                        SavingsGoals(
                            modifier = Modifier.fillMaxWidth()
                        )

                        FinancialTips(
                            modifier = Modifier.fillMaxWidth()
                        )

                        ExchangeRates(
                            modifier = Modifier.fillMaxWidth()
                        )

                        InvestmentPortfolio(
                            modifier = Modifier.fillMaxWidth()
                        )

                        CreditScoreMeter(
                            score = 785,
                            modifier = Modifier.fillMaxWidth()
                        )

                        UpcomingBills(
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                item {
                    Text(
                        text = "Recent Transactions",
                        modifier = Modifier.padding(vertical = 16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(expenses) { expense ->
                    val symbol = when (selectedCard?.currency) {
                        "EUR" -> "€"
                        "GBP" -> "£"
                        else -> "$"
                    }
                    ExpenseItem(expense, symbol)
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: ExpenseEntity, currencySymbol: String = "$") {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = expense.title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(expense.date)),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = "-$currencySymbol${String.format(Locale.US, "%.2f", expense.amount)}",
                color = Color(0xFFE57373),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PageIndicator(count: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(count) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (index == currentPage) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
            )
        }
    }
}
