package com.example.Canteen_Pos.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.Canteen_Pos.R
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.example.Canteen_Pos.DAO.UserDao
import com.example.Canteen_Pos.data.AppDatabase
import com.example.Canteen_Pos.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        val db = AppDatabase.getDatabase(applicationContext)
        userDao = db.userDao()

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val existingUser = userDao.getUserByUsername(username)
                if (existingUser != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val newUser = User(username, password, false)
                    userDao.insertUser(newUser)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}