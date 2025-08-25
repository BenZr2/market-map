package com.example.marketmap.ui.shopping_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketmap.data.AppDatabase
import com.example.marketmap.data.ShoppingItemDao
import com.example.marketmap.data.tables.ShoppingItem
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ShoppingListViewModel(private val db: AppDatabase) : ViewModel() {
    private val logger = KotlinLogging.logger {}
    private val dao: ShoppingItemDao = db.shoppingItemDao()

    private val _allItems = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val allItems: StateFlow<List<ShoppingItem>> = _allItems

    private val _selectedItem = MutableStateFlow<ShoppingItem?>(null)
    val selectedItem: StateFlow<ShoppingItem?> = _selectedItem

    init {
        viewModelScope.launch {
            dao.selectAll().collectLatest { list ->
                logger.info { "Select all from DB: $list" }
                _allItems.value = list
            }
        }
    }

    fun focusLost() {
        logger.info { "Focus lost" }
    }

    fun addItem(name: String) {
        var item = ShoppingItem(name = name)
        viewModelScope.launch {
            logger.info { "Insert into DB: $name" }
            stopEditing()
            val id = dao.insert(item)
            item = dao.selectById(id).first()
            startEditing(item)
        }
    }

    fun removeItem(item: ShoppingItem) {
        viewModelScope.launch {
            logger.info { "Delete from DB: $item" }
            stopEditing()
            dao.delete(item)
        }
    }

    fun toggleChecked(item: ShoppingItem) {
        viewModelScope.launch {
            logger.info { "Update isChecked in DB: $item" }
            stopEditing()
            dao.update(item.copy(isChecked = !item.isChecked))
        }
    }

    fun updateItem(item: ShoppingItem, newName: String) {
        viewModelScope.launch {
            logger.info { "Update name in DB: $item with $newName" }
            //stopEditing()
            dao.update(item.copy(name = newName))
        }
    }

    fun startEditing(item: ShoppingItem) {
        logger.info { "Start editing $item" }
        _selectedItem.value = item
    }

    fun stopEditing() {
        logger.info { "Stop editing" }
        _selectedItem.value = null
    }
}
