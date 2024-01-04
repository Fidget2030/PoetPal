package com.example.poetpal.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun AboutSidePanel(toggleSidePanel: () -> Unit) {
    Column {
        CenterAlignedTopAppBar(
            colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = { Text("Help") },
            actions = {
                IconButton(onClick = toggleSidePanel) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Close sidepanel")
                }
            },
        )
        Text(
            text =
            """Poetry is set apart from other forms of literature in how it plays with language.
                |Some forms of poetry are very free-flowing, and showcast creative uses,
                |but this can be challenging for someone just beginning. 
                |"Blank Page Syndrome" refers to the problem many have with having too much freedom,
                | and not knowing where to begin.
                |Some more structured forms of poetry can help you on your way, and stimulate you to think creatively.
            """
                .trimMargin(),
        )
    }
}
