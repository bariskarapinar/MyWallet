package com.myapp.mywallet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.myapp.mywallet.data.local.dao.CardDao
import com.myapp.mywallet.data.local.dao.ExpenseDao
import com.myapp.mywallet.data.local.entity.CardEntity
import com.myapp.mywallet.data.local.entity.ExpenseEntity

@Database(entities = [CardEntity::class, ExpenseEntity::class], version = 4, exportSchema = true)
abstract class MyWalletDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun expenseDao(): ExpenseDao
}
