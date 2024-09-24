package com.example.Canteen_Pos.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "transactions")
data class TransactionRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "transaction_id") val transactionId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "quantity") val quantity: Int,
    @ColumnInfo(name = "subtotal") val subtotal: Double,
    @ColumnInfo(name = "vat_rate") val vatRate: Double,
    @ColumnInfo(name = "vat_amount") val vatAmount: Double,
    @ColumnInfo(name = "discount_rate") val discountRate: Double,
    @ColumnInfo(name = "discount_amount") val discountAmount: Double,
    @ColumnInfo(name = "total") val total: Double,
    @ColumnInfo(name = "receipt_number") val receiptNumber: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "payment_method") val paymentMethod: String,
    @ColumnInfo(name = "ar") val ar: Double,
    @ColumnInfo(name = "transacnote") val note: String
)