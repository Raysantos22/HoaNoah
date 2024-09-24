package com.example.Canteen_Pos.ui.ViewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.Canteen_Pos.database.Product
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
/*
import com.example.Canteen_Pos.RetrofitClient
*/
import com.example.Canteen_Pos.data.AppDatabase
import com.example.Canteen_Pos.data.ProductRepository


class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductRepository
    val allProducts: LiveData<List<Product>>




    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)
        allProducts = repository.allProducts.asLiveData()
    }



    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun deleteAllProducts() {
        viewModelScope.launch {
            repository.deleteAllProducts()
        }
    }
    /* init {
           val productDao = AppDatabase.getDatabase(application).productDao()
           repository = ProductRepository(productDao, RetrofitClient.instance)
           allProducts = repository.allProducts.asLiveData()
       }*/

    /*   fun refreshProducts() = viewModelScope.launch {
           repository.refreshProducts()
       }
   */

    /*  fun insert(product: Product) = viewModelScope.launch {
        repository.insert(product)
    }

    fun update(product: Product) = viewModelScope.launch {
        repository.update(product)
    }

    fun updateCartItemQuantity(productId: Int, quantity: Int) = viewModelScope.launch {
        val product = repository.getProductById(productId)
        product?.let {
            it.quantity = quantity
            repository.update(it)
        }
    }

    fun removeFromCart(productId: Int) = viewModelScope.launch {
        val product = repository.getProductById(productId)
        product?.let {
            it.quantity = 0
            repository.update(it)
        }
    }

    fun clearCart() = viewModelScope.launch {
        val products = repository.allProducts.first()
        products.forEach {
            it.quantity = 0
            repository.update(it)
        }
    }
}
*/
    class ProductViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}