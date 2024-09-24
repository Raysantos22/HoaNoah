package com.example.Canteen_Pos.ui.ViewModel
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Canteen_Pos.data.CartRepository
import com.example.Canteen_Pos.database.CartItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    val allCartItems: StateFlow<List<CartItem>> = repository.allCartItems.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun insert(cartItem: CartItem) = viewModelScope.launch {
        repository.insert(cartItem)
    }

    fun update(cartItem: CartItem) = viewModelScope.launch {
        repository.update(cartItem)
    }

    fun delete(cartItem: CartItem) = viewModelScope.launch {
        repository.delete(cartItem)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    suspend fun getCartItemByProductId(productId: Int): CartItem? {
        return repository.getCartItemByProductId(productId)
    }
}
class CartViewModelFactory(private val repository: CartRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}