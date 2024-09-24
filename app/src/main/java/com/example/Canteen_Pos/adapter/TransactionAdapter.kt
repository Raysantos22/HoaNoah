package com.example.Canteen_Pos.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Canteen_Pos.R
import com.example.Canteen_Pos.database.TransactionRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(
    private var transactions: List<TransactionRecord>,
    private val onViewClick: (TransactionRecord) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionId: TextView = itemView.findViewById(R.id.transactionId)
        val transactionName: TextView = itemView.findViewById(R.id.transactionName)
        val transactionTotal: TextView = itemView.findViewById(R.id.transactionTotal)
        val transactionTimestamp: TextView = itemView.findViewById(R.id.transactionTimestamp)
        val quantity: TextView = itemView.findViewById(R.id.quantity)
        val paymentMethod: TextView = itemView.findViewById(R.id.paymentMethod)
        val receiptNumber: TextView = itemView.findViewById(R.id.receiptNumber)
        val arValue: TextView = itemView.findViewById(R.id.arValue)

        val viewButton: Button = itemView.findViewById(R.id.viewButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.transactionId.text = transaction.transactionId
        holder.transactionName.text = transaction.name
        holder.transactionTotal.text = "PHP${transaction.total}"
        holder.transactionTimestamp.text = formatDate(transaction.timestamp)
        holder.quantity.text = "Quantity: ${transaction.quantity}"
        holder.paymentMethod.text = transaction.paymentMethod
        holder.receiptNumber.text = "Receipt: ${transaction.receiptNumber}"
        holder.arValue.text = "AR: $${transaction.ar}"
        Log.d("TransactionAdapter", "Note for transaction ${transaction.transactionId}: ${transaction.note}")

        holder.viewButton.setOnClickListener {
            onViewClick(transaction)
        }
    }

    override fun getItemCount() = transactions.size

    fun updateTransactions(newTransactions: List<TransactionRecord>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}