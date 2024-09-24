package com.example.Canteen_Pos


import android.app.Application
import android.util.Log
import androidx.constraintlayout.helper.widget.MotionEffect.TAG
import com.example.Canteen_Pos.data.AppDatabase
import com.example.Canteen_Pos.data.CartRepository
import com.example.Canteen_Pos.data.ProductRepository

class ShoppingApplication : Application() {
    //  private val mySQLHelper by lazy { MySQLHelper() }
    private val database by lazy { AppDatabase.getDatabase(this) }

    //  val repository by lazy { ProductRepository(database.productDao(), productApi) }
    val cartRepository by lazy { CartRepository(database.cartDao()) }
    val productRepository by lazy { ProductRepository(database.productDao()) } // Change here
    //val productApi by lazy { RetrofitClient.instance }


    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e(TAG, "Uncaught exception in thread ${thread.name}", throwable)
        }

    }
}