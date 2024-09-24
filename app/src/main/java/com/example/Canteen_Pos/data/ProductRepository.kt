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

    suspend fun refreshProducts() {
        withContext(Dispatchers.IO) {
            val remoteProducts = productApi.getAllProducts()
            productDao.deleteAll()
            productDao.insertAll(remoteProducts)
        }
    }

    suspend fun createProduct(product: Product) {
        withContext(Dispatchers.IO) {
            val response = productApi.createProduct(product)
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if (apiResponse.data is Product) {
                        productDao.insertProduct(apiResponse.data)
                    }
                }
            } else {
                // Handle error
            }
        }
    }

    suspend fun updateProduct(product: Product) {
        withContext(Dispatchers.IO) {
            val response = productApi.updateProduct(product.id, product)
            if (response.isSuccessful) {
                // Assuming you want to update the local database only if the API call was successful
                productDao.updateProduct(product)
            } else {
                // Handle error
            }
        }
    }

    suspend fun deleteAllProducts() {
        withContext(Dispatchers.IO) {
            productApi.deleteAllProducts()
            productDao.deleteAll()
        }
    }

    suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }
}


