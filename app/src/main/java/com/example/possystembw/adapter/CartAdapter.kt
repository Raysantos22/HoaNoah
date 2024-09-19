package com.example.possystembw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.possystembw.R
import com.example.possystembw.database.CartItem

class CartAdapter(private val onItemClick: (CartItem) -> Unit) :
    ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item_layout, parent, false)
        return CartViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class CartViewHolder(itemView: View, val onItemClick: (CartItem) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val productNameView: TextView = itemView.findViewById(R.id.textViewCartProductName)
        private val productQuantityView: TextView = itemView.findViewById(R.id.textViewCartProductQuantity)
        private val productPriceView: TextView = itemView.findViewById(R.id.textViewCartProductPrice)

        fun bind(cartItem: CartItem) {
            productNameView.text = cartItem.productName
            productQuantityView.text = "Qty: ${cartItem.quantity}"
            productPriceView.text = "P${String.format("%.2f", cartItem.price)}"
            itemView.setOnClickListener { onItemClick(cartItem) }
        }
    }
}

class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
        return oldItem == newItem
    }
}