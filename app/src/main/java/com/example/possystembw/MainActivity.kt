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

        val button:Button = findViewById(R.id.partyCakesbtn)
        button.setOnClickListener{
            val intent = Intent(this, PartyCakes::class.java)
            startActivity(intent)
        }



    }
    }
