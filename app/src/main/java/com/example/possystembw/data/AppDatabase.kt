package com.example.possystembw.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import com.example.possystembw.DAO.CartDao
import com.example.possystembw.DAO.ProductDao
import com.example.possystembw.DAO.TransactionDao
import com.example.possystembw.database.CartItem
import com.example.possystembw.database.Product
import com.example.possystembw.database.TransactionRecord

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit


@Database(entities = [Product::class, CartItem::class, TransactionRecord::class], version = 7)  // Change version to 6
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        private const val TAG = "AppDatabase"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_6_7) // Add all migrations
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `transactions` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `transaction_id` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `price` REAL NOT NULL,
                        `quantity` INTEGER NOT NULL,
                        `receipt_number` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL
                    )
                    """
                )
            }
        }

        // Add migrations for versions 2 to 5 if necessary

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE transactions ADD COLUMN subtotal REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE transactions ADD COLUMN vat_rate REAL NOT NULL DEFAULT 0.12")
                database.execSQL("ALTER TABLE transactions ADD COLUMN vat_amount REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE transactions ADD COLUMN discount_rate REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE transactions ADD COLUMN discount_amount REAL NOT NULL DEFAULT 0.0")
            }
        }
    }
}
