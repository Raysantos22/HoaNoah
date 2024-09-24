package com.example.Canteen_Pos.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.Canteen_Pos.DAO.UserDao
import com.example.Canteen_Pos.R
import com.example.Canteen_Pos.data.AppDatabase
import com.example.Canteen_Pos.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        val db = AppDatabase.getDatabase(applicationContext)
        userDao = db.userDao()

        // Add a default admin user if not exists
        CoroutineScope(Dispatchers.IO).launch {
            if (userDao.getUser("admin", "admin") == null) {
                userDao.insertUser(User("admin", "admin", true))
            }
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            login(username, password)
        }

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            register(username, password)
        }
    }

    private fun login(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUser(username, password)
            withContext(Dispatchers.Main) {
                if (user != null) {
                    if (user.isAdmin) {
                        startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
                    } else {
                        startActivity(Intent(this@LoginActivity, UserActivity::class.java))
                    }
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun register(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser != null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                }
            } else {
                val newUser = User(username, password, false)
                userDao.insertUser(newUser)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    login(username, password)
                }
            }
        }
    }
}