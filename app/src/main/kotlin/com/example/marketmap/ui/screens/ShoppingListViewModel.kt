package com.example.marketmap.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.marketmap.data.model.ShoppingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ShoppingListViewModel: ViewModel() {
    private var nextId = 0

    private val _items = mutableStateListOf<ShoppingItem>()
    val items: List<ShoppingItem> = _items

    private val _editingItem = MutableStateFlow<ShoppingItem?>(null)
    val editingItem: StateFlow<ShoppingItem?> = _editingItem

    private val _editingItemText = MutableStateFlow<String?>(null)
    var editingItemText: StateFlow<String?> = _editingItemText

    fun addItem(name: String) {
        _items.add(ShoppingItem(id = nextId++, name = name))
    }

    fun removeItem(item: ShoppingItem) {
        _items.remove(item)
    }

    fun toggleChecked(item: ShoppingItem) {
        val index = _items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            val currentItem = _items[index]
            _items[index] = currentItem.copy(isChecked = !currentItem.isChecked)
        }
    }

    fun updateItem(item: ShoppingItem?, newName: String) {
        val index = _items.indexOfFirst { it.id == item?.id }
        if (index != -1) {
            val currentItem = _items[index]
            _items[index] = currentItem.copy(name = newName)
        }
    }

    fun startEditing(item: ShoppingItem) {
        editingItem.value.let {
            updateItem(editingItem.value, editingItemText.value ?: "")
        }
        _editingItem.value = item
    }

    fun stopEditing() {
        _editingItem.value = null
    }

    fun updateEditingItemText(newString: String) {
        _editingItemText.value = newString
    } }