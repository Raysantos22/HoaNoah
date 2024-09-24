package com.example.possystembw.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.possystembw.R

class PartyCakes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_party_cakes)


        val button: Button = findViewById(R.id.PurchaseWindow1)
        button.setOnClickListener{
            val intent = Intent(this, Window1::class.java)
            startActivity(intent)



        }

    }
}