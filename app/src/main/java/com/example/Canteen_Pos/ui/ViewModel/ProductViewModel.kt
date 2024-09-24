package com.example.possystembw.ui.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.possystembw.database.Product
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.possystembw.data.AppDatabase
import com.example.possystembw.data.ProductRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach


class ProductViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ProductRepository
    val allProducts: LiveData<List<Product>>

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao, RetrofitClient.instance)
        allProducts = repository.allProducts.asLiveData()
    }

    fun refreshProducts() = viewModelScope.launch {
        repository.refreshProducts()
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