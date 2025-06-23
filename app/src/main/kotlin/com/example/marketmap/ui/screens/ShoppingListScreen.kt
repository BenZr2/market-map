package com.example.marketmap.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.marketmap.data.model.ShoppingItem

/**
 * Show list
 * Add items
 * Remove item
 * Checkbox item
 */

@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel = ShoppingListViewModel()) {
    var newItemText by remember { mutableStateOf("") }

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text("Shopping list", style = MaterialTheme.typography.headlineMedium)

            LazyColumn {
                if (viewModel.items.isEmpty()) {
                    viewModel.addItem("")
                }
                items(viewModel.items) { item ->
                    ShoppingListItemRow(item = item, viewModel = viewModel)
                }
            }
        }
    }

}

@Composable
fun ShoppingListItemRow(item: ShoppingItem, viewModel: ShoppingListViewModel) {
    val editingItem by viewModel.editingItem.collectAsState()
    var editing = editingItem?.id == item.id
    var editText by remember { mutableStateOf(item.name) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                viewModel.startEditing(item)
            }
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = {
                viewModel.toggleChecked(item)
                editing = false
            }
        )
        if (editing) {
            TextField(
                value = editText,
                onValueChange = {
                    editText = it
                    viewModel.updateEditingItemText(editText)
                },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.updateItem(item, editText)
                        viewModel.addItem("")
                        viewModel.stopEditing()
                        editing = false
                    }
                )
            )
        } else {
            Text(
                text = item.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                style = if (item.isChecked) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else {
                    LocalTextStyle.current
                }
            )
        }

        IconButton(onClick = {
            viewModel.removeItem(item)
            viewModel.stopEditing()
        }) {
            Icon(Icons.Default.Delete, contentDescription = "delete")
        }
    }
}
