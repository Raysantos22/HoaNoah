package com.example.Canteen_Pos.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Canteen_Pos.R
import com.example.Canteen_Pos.adapter.TransactionAdapter
import com.example.Canteen_Pos.database.TransactionRecord
import com.example.Canteen_Pos.ui.ViewModel.TransactionViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionView : AppCompatActivity() {
    private lateinit var viewModel: TransactionViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transaction_view)

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        setupRecyclerView()
        setupGenerateExcelButton()
        observeTransactions()
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(emptyList()) { transaction ->
            showTransactionDialog(transaction)
        }
        val recyclerView: RecyclerView = findViewById(R.id.transactionRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transactionAdapter
    }

    private fun setupGenerateExcelButton() {
        findViewById<Button>(R.id.generateExcelButton).setOnClickListener {
            handleGenerateExcelButtonClick()
        }
    }

    private fun observeTransactions() {
        viewModel.allTransactions.observe(this) { transactions ->
            transactionAdapter.updateTransactions(transactions)
        }
    }
    private fun showTransactionDialog(transaction: TransactionRecord) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Transaction Details")
            .setMessage(
                """
            ID: ${transaction.transactionId}
            Name: ${transaction.name}
            Price: PHP${transaction.price}
            Quantity: ${transaction.quantity}
            Subtotal: PHP${transaction.subtotal}
            VAT Rate: ${transaction.vatRate}%
            VAT Amount: PHP${transaction.vatAmount}
            Discount Rate: ${transaction.discountRate}%
            Discount Amount: PHP${transaction.discountAmount}
            Total: PHP${transaction.total}
            Receipt Number: ${transaction.receiptNumber}
            Date: ${formatDate(transaction.timestamp)}
            Payment Method: ${transaction.paymentMethod}
            AR: $${transaction.ar}
            Note: ${transaction.note.takeIf { it.isNotBlank() } ?: "N/A"}
            """.trimIndent()
            )
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .create()
        dialog.show()
    }


    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    private fun handleGenerateExcelButtonClick() {
        val startDateStr = findViewById<EditText>(R.id.startDate).text.toString()
        val endDateStr = findViewById<EditText>(R.id.endDate).text.toString()

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(this, "Please enter both start and end dates", Toast.LENGTH_SHORT).show()
            return
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val start = try {
            dateFormat.parse(startDateStr)?.time ?: 0
        } catch (e: ParseException) {
            Toast.makeText(this, "Invalid start date format", Toast.LENGTH_SHORT).show()
            return
        }

        val end = try {
            dateFormat.parse(endDateStr)?.time ?: System.currentTimeMillis()
        } catch (e: ParseException) {
            Toast.makeText(this, "Invalid end date format", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.getTransactionsByDateRange(start, end).observe(this) { transactions ->
            transactionAdapter.updateTransactions(transactions)
            generateExcelFile(transactions)
        }
    }

    private fun generateExcelFile(transactions: List<TransactionRecord>) {
        // Implement your Excel generation logic here
        // For now, just show a toast message
        Toast.makeText(this, "Excel file generated with ${transactions.size} transactions", Toast.LENGTH_SHORT).show()
    }
}