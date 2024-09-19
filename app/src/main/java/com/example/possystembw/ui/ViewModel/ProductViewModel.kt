package com.example.possystembw.ui.ViewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.possystembw.database.Product
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.possystembw.data.ProductRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    val allProducts: Flow<List<Product>> = repository.allProducts

    fun insert(product: Product) = viewModelScope.launch {
        repository.insert(product)
    }

    fun updateCartItemQuantity(productId: Int, quantity: Int) = viewModelScope.launch {
        val product = repository.getProductById(productId)
        product?.let {
            it.quantity = quantity
            repository.insert(it)
        }
    }

    fun removeFromCart(productId: Int) = viewModelScope.launch {
        val product = repository.getProductById(productId)
        product?.let {
            it.quantity = 0
            repository.insert(it)
        }
    }

    fun clearCart() = viewModelScope.launch {
        val products = repository.allProducts.first()
        products.forEach {
            it.quantity = 0
            repository.insert(it)
        }
    }
}

class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
