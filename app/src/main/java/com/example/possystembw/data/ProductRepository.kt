package com.example.possystembw.data
import com.example.possystembw.DAO.ProductDao
import com.example.possystembw.database.Product
import com.example.possystembw.DAO.ProductApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productDao: ProductDao,
    private val productApi: ProductApi
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()
    suspend fun refreshProducts() { withContext(Dispatchers.IO) {
                val remoteProducts = productApi.getAllProducts()
                productDao.deleteAll()
                productDao.insertAll(remoteProducts)
        }
    }
    suspend fun createProduct(product: Product) {
        withContext(Dispatchers.IO) {
                val createdProduct = productApi.createProduct(product)
                productDao.insertProduct(createdProduct)
        }
    }
    suspend fun updateProduct(product: Product) { withContext(Dispatchers.IO) {
                val updatedProduct = productApi.updateProduct(product.id, product)
                productDao.updateProduct(updatedProduct)
        }
    }
    suspend fun deleteAllProducts() { withContext(Dispatchers.IO) {
                productApi.deleteAllProducts()
                productDao.deleteAll()
        }
    }
    suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }
}

