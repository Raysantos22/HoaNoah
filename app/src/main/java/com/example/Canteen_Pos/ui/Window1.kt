package com.example.possystembw.ui

import android.os.Bundle
import android.util.Log
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
import com.example.possystembw.ui.ViewModel.CartViewModel
import com.example.possystembw.ui.ViewModel.CartViewModelFactory
import com.example.possystembw.database.Product
import com.example.possystembw.database.CartItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import com.example.possystembw.AutoDatabaseTransferManager
import com.example.possystembw.DAO.TransactionDao
import com.example.possystembw.data.AppDatabase
import com.example.possystembw.database.TransactionRecord
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

class Window1 : AppCompatActivity() {
    private lateinit var autoDatabaseTransferManager: AutoDatabaseTransferManager
    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var totalAmountTextView: TextView
    private lateinit var payButton: Button
    private val TAG = "Window1"
    private lateinit var transactionDao: TransactionDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_window1)
            Log.d(TAG, "setContentView completed")
            // Initialize UI components
            totalAmountTextView = findViewById(R.id.totalAmountTextView)
            payButton = findViewById(R.id.payButton)

            autoDatabaseTransferManager = AutoDatabaseTransferManager(this, lifecycleScope)
            autoDatabaseTransferManager.startMonitoringConnectivity()
            // Set up RecyclerView for products
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
            val productAdapter = ProductAdapter { product ->
                addToCart(product)
            }
            recyclerView.adapter = productAdapter
            recyclerView.layoutManager = GridLayoutManager(this, 5)
            val database = AppDatabase.getDatabase(this)
            transactionDao = database.transactionDao()
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
            productViewModel = ViewModelProvider(
                this,
                ProductViewModel.ProductViewModelFactory(application)
            )
                .get(ProductViewModel::class.java)
            cartViewModel = ViewModelProvider(this, CartViewModelFactory(cartRepository))
                .get(CartViewModel::class.java)
            // Sync with MySQL
            // Observe products and update the adapter
            lifecycleScope.launch {
                productViewModel.allProducts.observe(this@Window1) { products ->
                    productAdapter.submitList(products)
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
            payButton.setOnClickListener {
                showPaymentDialog()
            }
            Log.d(TAG, "onCreate completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        autoDatabaseTransferManager.stopMonitoringConnectivity()
        Log.d(TAG, "onDestroy called")
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

    private suspend fun addTransaction(
        cartItems: List<CartItem>,
        receiptNumber: String,
        paymentMethod: String,
        ar: Double,
        total: Double,
        vatRate: Double,
        discountAmount: Double
    ) {
        val transactionId = UUID.randomUUID().toString()
        val discountRate = if (total > 0) discountAmount / total else 0.0

        cartItems.forEach { cartItem ->
            val itemTotal = cartItem.price * cartItem.quantity
            val itemDiscount = (discountAmount / total) * itemTotal
            val itemVat = (itemTotal - itemDiscount) * vatRate
            val transactionRecord = TransactionRecord(
                transactionId = transactionId,
                name = cartItem.productName,
                price = cartItem.price,
                quantity = cartItem.quantity,
                subtotal = itemTotal,
                vatRate = vatRate,
                vatAmount = itemVat,
                discountRate = discountRate,
                discountAmount = itemDiscount,
                total = itemTotal - itemDiscount + itemVat,
                receiptNumber = receiptNumber,
                paymentMethod = paymentMethod,
                ar = if (ar > 0.0) (itemTotal - itemDiscount + itemVat) else 0.0
            )
            transactionDao.insert(transactionRecord)
            Log.d(TAG, "Added transaction: $transactionRecord")
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
        val paymentMethodSpinner = dialogView.findViewById<Spinner>(R.id.paymentMethodSpinner)
        val vatSpinner = dialogView.findViewById<Spinner>(R.id.vatSpinner)
        val discountSpinner = dialogView.findViewById<Spinner>(R.id.discountSpinner)
        val discountAmountEditText = dialogView.findViewById<EditText>(R.id.discountAmountEditText)

        // Set up the spinner with payment methods
        val paymentMethods =
            arrayOf("Cash", "Credit Card", "Debit Card", "Bank Transfer", "Gcash", "UTANG")
        val paymentAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, paymentMethods)
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        paymentMethodSpinner.adapter = paymentAdapter

        // Set up the VAT spinner
        val vatOptions = arrayOf("12%", "0%")
        val vatAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, vatOptions)
        vatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        vatSpinner.adapter = vatAdapter

        // Set up the discount spinner
        val discountOptions = arrayOf("No Discount", "Percentage", "Fixed Amount")
        val discountAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, discountOptions)
        discountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        discountSpinner.adapter = discountAdapter

        // Show/hide discount amount input based on discount spinner selection
        discountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                discountAmountEditText.visibility = if (position != 0) View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        AlertDialog.Builder(this)
            .setTitle("Payment")
            .setView(dialogView)
            .setPositiveButton("Pay") { dialog, _ ->
                val amountPaid = amountPaidEditText.text.toString().toDoubleOrNull()
                val paymentMethod = paymentMethodSpinner.selectedItem.toString()
                val vatRate = if (vatSpinner.selectedItem.toString() == "12%") 0.12 else 0.0
                val discountType = discountSpinner.selectedItem.toString()
                val discountValue = discountAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
                if (amountPaid != null) {
                    processPayment(amountPaid, paymentMethod, vatRate, discountType, discountValue)
                } else {
                    Log.e(TAG, "Invalid amount entered")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun generateReceiptNumber(): String {
        return "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase()
    }

    private fun processPayment(
        amountPaid: Double,
        paymentMethod: String,
        vatRate: Double,
        discountType: String,
        discountValue: Double
    ) {
        lifecycleScope.launch {
            val cartItems = cartViewModel.allCartItems.value ?: emptyList()
            val subtotal = cartItems.sumOf { cartItem -> cartItem.price * cartItem.quantity }

            val discountAmount = when (discountType) {
                "Percentage" -> subtotal * (discountValue / 100)
                "Fixed Amount" -> discountValue
                else -> 0.0
            }

            // Calculate discountRate
            val discountRate = if (subtotal > 0) discountAmount / subtotal else 0.0

            val discountedSubtotal = subtotal - discountAmount
            val vatAmount = discountedSubtotal * vatRate
            val totalAmount = discountedSubtotal + vatAmount

            val change = amountPaid - totalAmount
            val ar =
                if (paymentMethod == "Credit Card" || paymentMethod == "Gcash" || paymentMethod == "UTANG") totalAmount else 0.0

            if (change >= 0.0) {
                val receiptNumber = generateReceiptNumber()
                addTransaction(
                    cartItems,
                    receiptNumber,
                    paymentMethod,
                    ar,
                    totalAmount,
                    vatRate,
                    discountAmount
                )
                showChangeAndReceiptDialog(
                    change,
                    cartItems,
                    receiptNumber,
                    paymentMethod,
                    ar,
                    vatAmount,
                    discountAmount,
                    totalAmount,
                    discountRate
                )
                cartViewModel.deleteAll()
            } else {
                Log.e(TAG, "Insufficient payment")
            }
        }
    }

    private fun showChangeAndReceiptDialog(
        change: Double,
        cartItems: List<CartItem>,
        receiptNumber: String,
        paymentMethod: String,
        ar: Double,
        vatAmount: Double,
        discountAmount: Double,
        totalAmount: Double,
        discountRate: Double
    ) {
        val message = String.format(
            "Change: P%.2f\n" +
                    "Receipt Number: %s\n" +
                    "Payment Method: %s\n" +
                    "Subtotal: P%.2f\n" +
                    "Discount: P%.2f (%.2f%%)\n" +
                    "VAT: P%.2f\n" +
                    "Total: P%.2f\n" +
                    "Accounts receivable: P%.2f",
            change, receiptNumber, paymentMethod,
            totalAmount - vatAmount + discountAmount,
            discountAmount, discountRate * 100, vatAmount, totalAmount, ar
        )
        AlertDialog.Builder(this)
            .setTitle("Payment Successful")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                autoDatabaseTransferManager = AutoDatabaseTransferManager(this, lifecycleScope)
                autoDatabaseTransferManager.startMonitoringConnectivity()
            }
            .setNeutralButton("Print Receipt") { dialog, _ ->
                printReceipt(
                    cartItems,
                    change,
                    receiptNumber,
                    paymentMethod,
                    ar,
                    vatAmount,
                    discountAmount,
                    totalAmount,
                    discountRate
                )
                dialog.dismiss()
            }
            .show()
    }


    private fun printReceipt(
        cartItems: List<CartItem>,
        change: Double,
        receiptNumber: String,
        paymentMethod: String,
        ar: Double,
        vatAmount: Double,
        discountAmount: Double,
        totalAmount: Double,
        discountRate: Double
    ) {
        // Implement receipt printing logic here
        // Include new fields in the receipt
        Log.d(
            TAG,
            "Printing receipt... Receipt Number: $receiptNumber, Payment Method: $paymentMethod, AR: $ar, VAT: $vatAmount, Discount: $discountAmount (${discountRate * 100}%), Total: $totalAmount"
        )
    }
}