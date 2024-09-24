package com.example.Canteen_Pos.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Canteen_Pos.R
import com.example.Canteen_Pos.ShoppingApplication
import com.example.Canteen_Pos.adapter.ProductAdapter
import com.example.Canteen_Pos.database.Product
import com.example.Canteen_Pos.data.ProductRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductManagementActivity : AppCompatActivity() {

    private lateinit var productRepository: ProductRepository
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_management)

        // Get the productRepository from ShoppingApplication
        productRepository = (application as ShoppingApplication).productRepository

        val recyclerView: RecyclerView = findViewById(R.id.productRecyclerView)
        productAdapter = ProductAdapter(onItemClick = { product -> showEditProductDialog(product) })
        recyclerView.adapter = productAdapter

        // Set LayoutManager
        recyclerView.layoutManager = GridLayoutManager(this, 5)

        val fabAddProduct: FloatingActionButton = findViewById(R.id.fabAddProduct)
        fabAddProduct.setOnClickListener { showAddProductDialog() }

        // Use viewLifecycleOwner.lifecycleScope
        lifecycleScope.launch {
            productRepository.allProducts.collectLatest { products ->
                productAdapter.submitList(products)
                Log.d("ProductManagementActivity", "Submitting list of ${products.size} products")
            }
        }
    }

    private fun showAddProductDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_product, null)
        AlertDialog.Builder(this)
            .setTitle("Add Product")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = dialogView.findViewById<EditText>(R.id.etProductName).text.toString().trim()
                val price = dialogView.findViewById<EditText>(R.id.etProductPrice).text.toString().toDoubleOrNull()
                val imageUrl = dialogView.findViewById<EditText>(R.id.etProductImageUrl).text.toString().trim()

                if (name.isEmpty() || price == null || imageUrl.isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please fill in all fields", Snackbar.LENGTH_LONG).show()
                    return@setPositiveButton
                }

                val newProduct = Product(name = name, price = price, imageUrl = imageUrl)
                addProduct(newProduct)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditProductDialog(product: Product) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit_product, null)
        dialogView.findViewById<EditText>(R.id.etProductName).setText(product.name)
        dialogView.findViewById<EditText>(R.id.etProductPrice).setText(product.price.toString())
        dialogView.findViewById<EditText>(R.id.etProductImageUrl).setText(product.imageUrl)

        AlertDialog.Builder(this)
            .setTitle("Edit Product")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedName = dialogView.findViewById<EditText>(R.id.etProductName).text.toString().trim()
                val updatedPrice = dialogView.findViewById<EditText>(R.id.etProductPrice).text.toString().toDoubleOrNull()
                val updatedImageUrl = dialogView.findViewById<EditText>(R.id.etProductImageUrl).text.toString().trim()

                if (updatedName.isEmpty() || updatedPrice == null || updatedImageUrl.isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content), "Please fill in all fields", Snackbar.LENGTH_LONG).show()
                    return@setPositiveButton
                }

                val updatedProduct = product.copy(
                    name = updatedName,
                    price = updatedPrice,
                    imageUrl = updatedImageUrl
                )
                updateProduct(updatedProduct)
            }
            .setNegativeButton("Cancel", null)
            .setNeutralButton("Delete") { _, _ -> showDeleteConfirmationDialog(product) }
            .show()
    }

    private fun showDeleteConfirmationDialog(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton("Delete") { _, _ -> deleteProduct(product) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addProduct(product: Product) {
        lifecycleScope.launch {
            try {
                productRepository.createProduct(product)
                Snackbar.make(findViewById(android.R.id.content), "Product added", Snackbar.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ProductManagementActivity", "Error adding product", e)
                Snackbar.make(findViewById(android.R.id.content), "Error adding product: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun updateProduct(product: Product) {
        lifecycleScope.launch {
            try {
                productRepository.updateProduct(product)
                Snackbar.make(findViewById(android.R.id.content), "Product updated", Snackbar.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("ProductManagementActivity", "Error updating product", e)
                Snackbar.make(findViewById(android.R.id.content), "Error updating product: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteProduct(product: Product) {
        lifecycleScope.launch {
            try {
                productRepository.deleteProduct(product)
                Snackbar.make(findViewById(android.R.id.content), "Product deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        lifecycleScope.launch {
                            productRepository.createProduct(product)
                        }
                    }.show()
            } catch (e: Exception) {
                Log.e("ProductManagementActivity", "Error deleting product", e)
                Snackbar.make(findViewById(android.R.id.content), "Error deleting product: ${e.message}", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
