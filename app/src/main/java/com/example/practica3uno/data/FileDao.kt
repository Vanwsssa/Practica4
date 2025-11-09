package com.example.practica3uno.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: FileEntity)

    @Query("SELECT * FROM files WHERE type = 'recent' ORDER BY timestamp DESC LIMIT 20")
    suspend fun getRecentFiles(): List<FileEntity>

    @Query("SELECT * FROM files WHERE type = 'favorite' ORDER BY timestamp DESC")
    suspend fun getFavoriteFiles(): List<FileEntity>

    @Query("DELETE FROM files WHERE uri = :uri")
    suspend fun deleteFile(uri: String)
}
