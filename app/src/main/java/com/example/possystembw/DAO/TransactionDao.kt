package com.example.possystembw.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.possystembw.database.TransactionRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transactionRecord: TransactionRecord)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionRecord>>

    @Query("SELECT * FROM transactions WHERE transaction_id = :transactionId")
    suspend fun getTransactionById(transactionId: String): TransactionRecord?

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}