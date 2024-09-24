package com.example.Canteen_Pos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Canteen_Pos.R
import com.example.Canteen_Pos.database.TransactionRecord

class TransactionAdapter(private var transactions: List<TransactionRecord>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val transactionId: TextView = itemView.findViewById(R.id.transactionId)
        val total: TextView = itemView.findViewById(R.id.total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.transactionId.text = transaction.transactionId
        holder.total.text = transaction.total.toString()
    }

    override fun getItemCount() = transactions.size

    fun updateTransactions(newTransactions: List<TransactionRecord>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
