package com.example.possystembw.model

import android.util.Log
import androidx.lifecycle.*
import com.example.possystembw.database.Product
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    val allProducts: Flow<List<Product>> = repository.allProducts.onEach { products ->
        Log.d("ProductViewModel", "Emitting ${products.size} products")
    }

    init {
        Log.d("ProductViewModel", "ViewModel initialized")
    }

    fun insert(product: Product) = viewModelScope.launch {
        repository.insert(product)
        Log.d("ProductViewModel", "Product inserted: $product")
    }
}

class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

