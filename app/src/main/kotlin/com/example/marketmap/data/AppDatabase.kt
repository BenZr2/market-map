package com.example.marketmap.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.marketmap.data.ShoppingItemDao
import com.example.marketmap.data.tables.ShoppingItem
import android.content.Context
import androidx.room.Room

@Database(entities = [ShoppingItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingItemDao(): ShoppingItemDao
}

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "shopping_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
