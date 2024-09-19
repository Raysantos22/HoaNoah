package com.example.possystembw


import android.app.Application
import com.example.possystembw.data.AppDatabase
import com.example.possystembw.data.CartRepository
import com.example.possystembw.data.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ShoppingApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ProductRepository(database.productDao()) }
    val cartRepository by lazy { CartRepository(database.cartDao()) }

    override fun onCreate() {
        super.onCreate()
    }
}