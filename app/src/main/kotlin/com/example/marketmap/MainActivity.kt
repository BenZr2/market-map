package com.example.marketmap

import com.example.marketmap.ui.shopping_list.ShoppingListViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.marketmap.data.DatabaseProvider
import com.example.marketmap.ui.shopping_list.ShoppingListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(applicationContext)
        val viewModel = ShoppingListViewModel(db)

        setContent {
            MaterialTheme {
                ShoppingListScreen(viewModel)
            }
        }
    }
}

