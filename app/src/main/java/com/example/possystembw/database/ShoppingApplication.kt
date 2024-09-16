package com.example.possystembw.database

import android.app.Application
import com.example.possystembw.model.AppDatabase
import com.example.possystembw.model.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ShoppingApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ProductRepository(database.productDao()) }

    override fun onCreate() {
        super.onCreate()
    }
}