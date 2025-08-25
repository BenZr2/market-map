package com.example.marketmap.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.marketmap.data.tables.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {
    @Query("SELECT * FROM shopping_items")
    fun selectAll(): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE id = :id")
    fun selectById(id: Long): Flow<ShoppingItem>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(item: ShoppingItem): Long

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)
}