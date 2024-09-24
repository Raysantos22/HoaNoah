package com.example.Canteen_Pos.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "is_admin") val isAdmin: Boolean
)
