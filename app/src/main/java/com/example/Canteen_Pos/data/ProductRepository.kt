package com.example.Canteen_Pos.data
import android.util.Log
import com.example.Canteen_Pos.DAO.ProductDao
import com.example.Canteen_Pos.database.Product
import com.example.Canteen_Pos.DAO.ProductApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productDao: ProductDao
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun createProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.insertProduct(product)
        }
    }

    suspend fun updateProduct(product: Product) {
        withContext(Dispatchers.IO) {
            productDao.updateProduct(product)
        }
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun deleteAllProducts() {
        withContext(Dispatchers.IO) {
            productDao.deleteAll()
        }
    }

    suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }
}



















/*  private val productDao: ProductDao,
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
          try {
              val response = productApi.createProduct(product)
              if (response.isSuccessful) {
                  response.body()?.let { apiResponse ->
                      val createdProduct = apiResponse.data
                      if (createdProduct is Product) {
                          productDao.insertProduct(createdProduct)
                      } else {
                          throw IllegalStateException("API returned unexpected data type")
                      }
                  } ?: throw IllegalStateException("API response body is null")
              } else {
                  // Log more details about the error response
                  Log.e("ProductRepository", "Error creating product. Code: ${response.code()}, Message: ${response.message()}")
                  Log.e("ProductRepository", "Error body: ${response.errorBody()?.string()}")
                  throw Exception("API error: ${response.code()} ${response.message()}")
              }
          } catch (e: Exception) {
              Log.e("ProductRepository", "Exception when creating product", e)
              throw e
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
  suspend fun deleteProduct(product: Product) {
      productDao.deleteProduct(product)
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
*/

