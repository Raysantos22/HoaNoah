package com.example.possystembw.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "product_id") val productId: Int,
    @ColumnInfo(name = "product_name") val productName: String,
    @ColumnInfo(name = "quantity") var quantity: Int,
    @ColumnInfo(name = "price") val price: Double
)