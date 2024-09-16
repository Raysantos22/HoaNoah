package com.example.possystembw.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.possystembw.R
import com.example.possystembw.database.Product

class ProductAdapter : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        Log.d("ProductAdapter", "onCreateViewHolder called")
        return ProductViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = getItem(position)
        Log.d("ProductAdapter", "Binding product at position $position: $current")
        holder.bind(current)
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameView: TextView = itemView.findViewById(R.id.textViewProductName)
        private val productPriceView: TextView = itemView.findViewById(R.id.textViewProductPrice)
        private val productImageView: ImageView = itemView.findViewById(R.id.imageViewProductImage)

        fun bind(product: Product) {
            Log.d("ProductAdapter", "Binding product: $product")
            productNameView.text = product.name
            productPriceView.text = "P${product.price}"
            // Use Glide to load image
            Glide.with(itemView.context)
                .load(product.imageUrl)
                .into(productImageView)
        }

        companion object {
            fun create(parent: ViewGroup): ProductViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product, parent, false)
                return ProductViewHolder(view)
            }
        }
    }
}
    class ProductsComparator : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

