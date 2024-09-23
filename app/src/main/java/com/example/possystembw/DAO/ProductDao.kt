package com.example.possystembw.DAO

import androidx.room.*
import com.example.possystembw.database.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

        @Query("SELECT * FROM products")
        fun getAllProducts(): Flow<List<Product>>

        @Query("SELECT COUNT(*) FROM products")
        suspend fun getProductCount(): Int

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertProduct(product: Product)

        @Update
        suspend fun updateProduct(product: Product)

        @Query("DELETE FROM products")
        suspend fun deleteAll()

        @Query("SELECT * FROM products WHERE id = :id")
        suspend fun getProductById(id: Int): Product?

        @Query("SELECT * FROM products WHERE name LIKE :name")
        fun getProductsByName(name: String): Flow<List<Product>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(products: List<Product>)


}





