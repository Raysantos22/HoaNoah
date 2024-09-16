package com.example.possystembw.model

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.possystembw.database.Product
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(product: Product) {
        productDao.insertProduct(product)
    }
}