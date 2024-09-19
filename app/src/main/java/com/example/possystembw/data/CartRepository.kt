package com.example.possystembw.data

import com.example.possystembw.DAO.CartDao
import com.example.possystembw.DAO.ProductDao
import com.example.possystembw.database.CartItem
import com.example.possystembw.database.Product
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {
    val allCartItems: Flow<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun insert(cartItem: CartItem) {
        cartDao.insert(cartItem)
    }

    suspend fun update(cartItem: CartItem) {
        cartDao.update(cartItem)
    }

    suspend fun delete(cartItem: CartItem) {
        cartDao.delete(cartItem)
    }

    suspend fun deleteAll() {
        cartDao.deleteAll()
    }


    suspend fun getCartItemByProductId(productId: Int): CartItem? {
        return cartDao.getCartItemByProductId(productId)
    }
}