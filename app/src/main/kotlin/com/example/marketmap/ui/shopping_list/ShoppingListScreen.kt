package com.example.marketmap.ui.shopping_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketmap.data.tables.ShoppingItem
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * Show list
 * Add items
 * Remove item
 * Checkbox item
 */

@Composable
fun ShoppingListScreen(viewModel: ShoppingListViewModel) {
    val logger = KotlinLogging.logger {}
    val allItems by viewModel.allItems.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Shopping list", style = MaterialTheme.typography.headlineMedium)

            LazyColumn {
                items(allItems) { item ->
                    ShoppingListItemRow(item = item, viewModel = viewModel)
                }
                item {
                    AddItem(viewModel)
                }
            }
        }
    }
}

@Composable
fun ShoppingListItemRow(item: ShoppingItem, viewModel: ShoppingListViewModel) {
    val selectedItem by viewModel.selectedItem.collectAsState()
    var selectedItemText by remember { mutableStateOf(TextFieldValue(item.name)) }
    var isEditing = selectedItem?.id == item.id
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            selectedItemText = TextFieldValue(
                text = item.name,
                selection = TextRange(item.name.length) // cursor to end
            )
            focusRequester.requestFocus()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = {
                viewModel.toggleChecked(item)
            }
        )
        if (isEditing) {
            TextField(
                value = selectedItemText,
                onValueChange = {
                    selectedItemText = it
                },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (hasFocus && !focusState.isFocused) {  // FIXME: while editing a unchecked item -> click check
                            viewModel.focusLost()
                            viewModel.updateItem(item, selectedItemText.text)
                        }
                        hasFocus = focusState.isFocused
                    },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        viewModel.stopEditing()
                        viewModel.updateItem(item, selectedItemText.text)
                    }
                )
            )
        } else {
            Text(
                text = item.name,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .padding(horizontal = 8.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .clickable {
                        viewModel.startEditing(item)
                    },
                style = if (item.isChecked) {
                    LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                } else {
                    LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    )
                }
            )
        }

        IconButton(onClick = {
            viewModel.removeItem(item)
        }) {
            Icon(Icons.Default.Delete, contentDescription = "delete")
        }
    }
}


@Composable
fun AddItem(viewModel: ShoppingListViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                viewModel.addItem("")
            }
    ) {
        IconButton(onClick = {
            viewModel.addItem("")
        }) {
            Icon(Icons.Default.Add, contentDescription = "add")
        }
    }
}