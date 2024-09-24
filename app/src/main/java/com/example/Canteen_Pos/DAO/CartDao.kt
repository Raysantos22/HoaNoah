package com.example.Canteen_Pos.DAO

import androidx.room.*
import com.example.Canteen_Pos.database.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Update
    suspend fun update(cartItem: CartItem)

    @Delete
    suspend fun delete(cartItem: CartItem)

    @Query("DELETE FROM cart_items")
    suspend fun deleteAll()

    @Query("SELECT * FROM cart_items WHERE product_id = :productId")
    suspend fun getCartItemByProductId(productId: Int): CartItem?

    // Add this function
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cartItems: List<CartItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem)
}