package com.example.Canteen_Pos.ui.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.Canteen_Pos.database.TransactionRecord
import com.example.Canteen_Pos.DAO.TransactionDao
import com.example.Canteen_Pos.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val transactionDao: TransactionDao
    val allTransactions: LiveData<List<TransactionRecord>>

    init {
        val database = AppDatabase.getDatabase(application)
        transactionDao = database.transactionDao()
        allTransactions = transactionDao.getAllTransactions().asLiveData()
    }

    fun getTransactionsByDateRange(start: Long, end: Long): LiveData<List<TransactionRecord>> {
        return transactionDao.getTransactionsByDateRange(start, end).asLiveData()
    }

    fun insertTransaction(transaction: TransactionRecord) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionDao.insert(transaction)
        }
    }

    // Add other methods as needed
}