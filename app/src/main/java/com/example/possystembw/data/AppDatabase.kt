package com.example.possystembw.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.possystembw.DAO.CartDao
import com.example.possystembw.DAO.ProductDao
import com.example.possystembw.database.CartItem
import com.example.possystembw.database.Product
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit


@Database(entities = [Product::class, CartItem::class], version = 5)  // Change version to 5
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao

    companion object {
        private const val TAG = "AppDatabase"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                try {
                    Log.d(TAG, "Creating database instance")
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "Pastry_database"
                    )
                        .addCallback(DatabaseAssetCallback(context))
                        .fallbackToDestructiveMigration()  // Add this line
                        .build()


                    INSTANCE = instance
                    instance
                } catch (e: Exception) {
                    Log.e(TAG, "Error creating database", e)
                    throw e
                }

            }

                }
            }
            }





