package com.example.possystembw.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.possystembw.R
import com.example.possystembw.adapter.ProductAdapter
import com.example.possystembw.ShoppingApplication
import com.example.possystembw.adapter.CartAdapter
import com.example.possystembw.ui.ViewModel.ProductViewModel
import com.example.possystembw.ui.ViewModel.ProductViewModelFactory
import com.example.possystembw.ui.ViewModel.CartViewModel
import com.example.possystembw.ui.ViewModel.CartViewModelFactory
import com.example.possystembw.database.Product
import com.example.possystembw.database.CartItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Window1 : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var totalAmountTextView: TextView
    private lateinit var payButton: Button
    private val TAG = "Window1"
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        Log.d(TAG, "onCreate: Started")

        try {
            setContentView(R.layout.activity_window1)
            Log.d(TAG, "setContentView completed")

            // Initialize UI components
            totalAmountTextView = findViewById(R.id.totalAmountTextView)
            payButton = findViewById(R.id.payButton)

            // Set up RecyclerView for products
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
            val productAdapter = ProductAdapter { product ->
                addToCart(product)
            }
            recyclerView.adapter = productAdapter
            recyclerView.layoutManager = GridLayoutManager(this, 5)

            // Set up RecyclerView for cart
            val recyclerViewCart = findViewById<RecyclerView>(R.id.recyclerviewcart)
            val cartAdapter = CartAdapter { cartItem ->
                removeFromCart(cartItem)
            }
            recyclerViewCart.adapter = cartAdapter
            recyclerViewCart.layoutManager = LinearLayoutManager(this)


            // Initialize ViewModels
            val repository = (application as? ShoppingApplication)?.repository
            val cartRepository = (application as? ShoppingApplication)?.cartRepository
            if (repository == null || cartRepository == null) {
                throw IllegalStateException("Repositories are null. Make sure ShoppingApplication is properly set up.")
            }

            productViewModel = ViewModelProvider(this, ProductViewModelFactory(repository))
                .get(ProductViewModel::class.java)
            cartViewModel = ViewModelProvider(this, CartViewModelFactory(cartRepository))
                .get(CartViewModel::class.java)

            // Observe products and update the adapter
            lifecycleScope.launch {
                productViewModel.allProducts.collectLatest { products ->
                    withContext(Dispatchers.Main) {
                        productAdapter.submitList(products)
                    }
                }
            }

            // Observe cart items, update the adapter and total amount
            lifecycleScope.launch {
                cartViewModel.allCartItems.collectLatest { cartItems ->
                    withContext(Dispatchers.Main) {
                        cartAdapter.submitList(cartItems)
                        updateTotalAmount(cartItems)
                    }
                }
            }

            // Set up pay button click listener
            payButton.setOnClickListener {
                showPaymentDialog()
            }

            Log.d(TAG, "onCreate completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            // Optionally, show an error dialog or toast to the user
        }
    }

    private fun addToCart(product: Product) {
        lifecycleScope.launch {
            val existingCartItem = cartViewModel.getCartItemByProductId(product.id)
            if (existingCartItem != null) {
                existingCartItem.quantity++
                cartViewModel.update(existingCartItem)
            } else {
                val newCartItem = CartItem(
                    productId = product.id,
                    productName = product.name,
                    quantity = 1,
                    price = product.price
                )
                cartViewModel.insert(newCartItem)
            }
        }
    }

    private fun removeFromCart(cartItem: CartItem) {
        lifecycleScope.launch {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
                cartViewModel.update(cartItem)
            } else {
                cartViewModel.delete(cartItem)
            }
        }
    }

    private fun updateTotalAmount(cartItems: List<CartItem>) {
        val total = cartItems.sumOf { cartItem -> cartItem.price * cartItem.quantity }
        totalAmountTextView.text = String.format("Total: P%.2f", total)
    }

    private fun showPaymentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_payment, null)
        val amountPaidEditText = dialogView.findViewById<EditText>(R.id.amountPaidEditText)

        AlertDialog.Builder(this)
            .setTitle("Payment")
            .setView(dialogView)
            .setPositiveButton("Pay") { dialog, _ ->
                val amountPaid = amountPaidEditText.text.toString().toDoubleOrNull()
                if (amountPaid != null) {
                    processPayment(amountPaid)
                } else {
                    // Handle invalid input
                    Log.e(TAG, "Invalid amount entered")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun processPayment(amountPaid: Double) {
        lifecycleScope.launch {
            val cartItems = cartViewModel.allCartItems.value ?: emptyList()
            val totalAmount = cartItems.sumOf { cartItem -> cartItem.price * cartItem.quantity }
            val change = amountPaid - totalAmount

            if (change >= 0.0) {
                showChangeAndReceiptDialog(change, cartItems)
                // Clear the cart after successful payment
                cartViewModel.deleteAll()
            } else {
                // Handle insufficient payment
                Log.e(TAG, "Insufficient payment")
            }
        }
    }

    private fun showChangeAndReceiptDialog(change: Double, cartItems: List<CartItem>) {
        val message = String.format("Change: P%.2f", change)

        AlertDialog.Builder(this)
            .setTitle("Payment Successful")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton("Print Receipt") { dialog, _ ->
                printReceipt(cartItems, change)
                dialog.dismiss()
            }
            .show()
    }

    private fun printReceipt(cartItems: List<CartItem>, change: Double) {
        // Implement receipt printing logic here
        // This could involve generating a PDF, sending to a printer, or displaying a receipt on screen
        Log.d(TAG, "Printing receipt...")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
}