package com.example.Canteen_Pos.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.Canteen_Pos.DAO.CartDao
import com.example.Canteen_Pos.DAO.ProductDao
import com.example.Canteen_Pos.DAO.TransactionDao
import com.example.Canteen_Pos.DAO.UserDao
import com.example.Canteen_Pos.database.CartItem
import com.example.Canteen_Pos.database.Product
import com.example.Canteen_Pos.database.TransactionRecord
import com.example.Canteen_Pos.database.User

@Database(entities = [Product::class, CartItem::class, TransactionRecord::class, User::class], version = 10)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun transactionDao(): TransactionDao
    abstract fun userDao(): UserDao

    companion object {
        private const val TAG = "AppDatabase"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Keep this new name if you want to start fresh
                )
                    .addMigrations(
                        MIGRATION_1_2, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9,MIGRATION_9_10
                    )
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

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE transactions ADD COLUMN subtotal REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE transactions ADD COLUMN vat_rate REAL NOT NULL DEFAULT 0.12")
                database.execSQL("ALTER TABLE transactions ADD COLUMN vat_amount REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE transactions ADD COLUMN discount_rate REAL NOT NULL DEFAULT 0.0")
                database.execSQL("ALTER TABLE transactions ADD COLUMN discount_amount REAL NOT NULL DEFAULT 0.0")
            }
        }
        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE products ADD COLUMN image_url TEXT")
            }
        }
        private val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `users` (
                        `username` TEXT PRIMARY KEY NOT NULL,
                        `password` TEXT NOT NULL,
                        `is_admin` INTEGER NOT NULL
                    )
                """)
            }
        }

        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE transactions ADD COLUMN transacnote TEXT")
            }
        }
    }
}


/*  // Add placeholder migrations for versions 2 to 5
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add migration steps if necessary
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add migration steps if necessary
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add migration steps if necessary
            }
        }
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add migration steps if necessary
            }
        }*/