package com.example.practica3uno.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey val uri: String,
    val name: String,
    val type: String, // "recent" o "favorite"
    val timestamp: Long
)
