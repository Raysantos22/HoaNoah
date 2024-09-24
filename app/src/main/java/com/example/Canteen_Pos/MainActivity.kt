package com.example.possystembw

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.possystembw.ui.PartyCakes


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val partyCakesButton: Button = findViewById(R.id.partyCakesbtn)
        partyCakesButton.setOnClickListener {
            val intent = Intent(this, PartyCakes::class.java)
            startActivity(intent)
        }

        val purchaseButton: Button = findViewById(R.id.Purchasebtn)
        purchaseButton.setOnClickListener {
            val intent = Intent(this, ProductManagementActivity::class.java)
            startActivity(intent)
        }
        val ResellerButton: Button = findViewById(R.id.ResellerBtn)
        ResellerButton.setOnClickListener {
            val intent = Intent(this, TransactionView::class.java)
            startActivity(intent)
        }
    }

}