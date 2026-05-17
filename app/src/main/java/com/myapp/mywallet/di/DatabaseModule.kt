package com.myapp.mywallet.di

import android.content.Context
import androidx.room.Room
import com.myapp.mywallet.data.local.MyWalletDatabase
import com.myapp.mywallet.data.local.dao.CardDao
import com.myapp.mywallet.data.local.dao.ExpenseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MyWalletDatabase {
        return Room.databaseBuilder(
            context,
            MyWalletDatabase::class.java,
            "my_wallet_db"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideCardDao(database: MyWalletDatabase): CardDao {
        return database.cardDao()
    }

    @Provides
    fun provideExpenseDao(database: MyWalletDatabase): ExpenseDao {
        return database.expenseDao()
    }
}
