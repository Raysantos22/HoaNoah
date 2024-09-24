package com.example.Canteen_Pos.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.Canteen_Pos.R

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user)

        val dashboard: ImageButton = findViewById(R.id.button2)
        dashboard.setOnClickListener {
            val intent = Intent(this, Window1::class.java)
            startActivity(intent)
        }

        val Pos: ImageButton = findViewById(R.id.button3)
        Pos.setOnClickListener {
            val intent = Intent(this, Window1::class.java)
            startActivity(intent)
        }
        val transactionRecord: ImageButton = findViewById(R.id.button6)
        transactionRecord.setOnClickListener {
            val intent = Intent(this, TransactionView::class.java)
            startActivity(intent)
        }
            val addmenu: ImageButton = findViewById(R.id.button7)
        addmenu.setOnClickListener {
                val intent = Intent(this, ProductManagementActivity::class.java)
                startActivity(intent)

        }
    }
}
