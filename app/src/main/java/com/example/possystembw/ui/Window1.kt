package com.example.possystembw.ui


import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.possystembw.R
import com.example.possystembw.adapter.ProductAdapter
import com.example.possystembw.database.Product
import com.example.possystembw.database.ShoppingApplication
import com.example.possystembw.model.ProductViewModel
import com.example.possystembw.model.ProductViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Window1 : AppCompatActivity() {
    private lateinit var productViewModel: ProductViewModel
    private val TAG = "Window1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d(TAG, "onCreate: Started")

        try {
            setContentView(R.layout.activity_window1)
            Log.d(TAG, "setContentView completed")

            // Set up RecyclerView
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

            val adapter = ProductAdapter()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(this, 5)

            // Initialize ViewModel
            val repository = (application as? ShoppingApplication)?.repository

            if (repository == null) {
                throw IllegalStateException("Repository is null. Make sure ShoppingApplication is properly set up.")
            }

            productViewModel = ViewModelProvider(this, ProductViewModelFactory(repository))
                .get(ProductViewModel::class.java)

            // Observe products and update the adapter
            lifecycleScope.launch {
                Log.d(TAG, "Starting to collect products")
                try {
                    productViewModel.allProducts.collect { products ->
                        withContext(Dispatchers.Main) {
                            adapter.submitList(products)
                        }
                    }
                } catch (e: Exception) {
                }
            }

            Log.d(TAG, "onCreate completed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            // Optionally, show an error dialog or toast to the user
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }
}

