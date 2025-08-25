    package com.example.marketmap.data.tables

    import androidx.room.Entity
    import androidx.room.PrimaryKey

    @Entity(tableName = "shopping_items")
    data class ShoppingItem(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        var name: String,
        var isChecked: Boolean = false
    )
