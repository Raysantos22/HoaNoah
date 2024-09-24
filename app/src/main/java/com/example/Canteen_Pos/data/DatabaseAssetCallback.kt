package com.example.Canteen_Pos.data
import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DatabaseAssetCallback(private val context: Context) : RoomDatabase.Callback() {
    private val TAG = "DatabaseAssetCallback"

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        Log.d(TAG, "onCreate called")
        copyDatabaseFromAsset()
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        Log.d(TAG, "onOpen called")
        // Check if the database has content
        val cursor = db.query("SELECT COUNT(*) FROM products")
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        Log.d(TAG, "Number of products in the database: $count")
        cursor.close()
    }

    private fun copyDatabaseFromAsset() {
        val dbPath = context.getDatabasePath("Pastry_database")
        Log.d(TAG, "Database path: $dbPath")

        // Make sure the path exists
        dbPath.parentFile?.let { parentFile ->
            if (!parentFile.exists()) {
                val created = parentFile.mkdirs()
                Log.d(TAG, "Parent directory created: $created")
            }
        }

        try {
            val assetManager = context.assets
            Log.d(TAG, "Asset files: ${assetManager.list("")?.joinToString()}")
            
            val inputStream = assetManager.open("Pastry_database.db")
            Log.d(TAG, "Opened asset input stream")
            val outputStream = FileOutputStream(dbPath)
            Log.d(TAG, "Opened output stream")

            inputStream.use { input ->
                outputStream.use { output ->
                    val bytesCopied = input.copyTo(output)
                    Log.d(TAG, "Bytes copied: $bytesCopied")
                }
            }

            Log.d(TAG, "Database copied successfully")
            
            // Verify the copied file
            val copiedFile = File(dbPath.toString())
            if (copiedFile.exists()) {
                Log.d(TAG, "Copied file exists. Size: ${copiedFile.length()} bytes")
            } else {
                Log.e(TAG, "Copied file does not exist!")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error copying database", e)
        }
    }
}