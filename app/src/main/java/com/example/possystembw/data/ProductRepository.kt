package com.example.possystembw.data

import androidx.annotation.WorkerThread
import com.example.possystembw.DAO.ProductDao
import com.example.possystembw.database.Product
import kotlinx.coroutines.flow.Flow


class ProductRepository(private val productDao: ProductDao) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun insert(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun getProductCount(): Int {
        return productDao.getProductCount()
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAll()
    }

    suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }

    fun getProductsByName(name: String): Flow<List<Product>> {
        return productDao.getProductsByName(name)
    }
}