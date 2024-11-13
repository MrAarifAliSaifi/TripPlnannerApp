package com.example.tripplnannerapp.base.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: String,
    val name: String,
    val location: String,
    val duration: String
)