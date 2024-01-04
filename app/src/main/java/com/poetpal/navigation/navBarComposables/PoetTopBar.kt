package com.poetpal.navigation.navBarComposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.poetpal.R

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun PoetTopBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    toggleSidePanel: () -> Unit,
    currentScreenTitle: Int,
) {
    CenterAlignedTopAppBar(
        colors =
        topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text(stringResource(id = currentScreenTitle)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "navigate back")
                }
            } else {
                IconButton(onClick = toggleSidePanel) {
                    Icon(
                        painter = painterResource(id = R.drawable.circle_question_solid_xl),
                        contentDescription = "Open side panel",
                    )
                }
            }
        },
    )
}
