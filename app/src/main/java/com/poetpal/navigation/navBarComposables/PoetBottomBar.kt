package com.poetpal.navigation.navBarComposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.poetpal.R

@Suppress("ktlint:standard:function-naming")
@Composable
fun PoetBottomBar(
    goToWrite: () -> Unit,
    goToRead: () -> Unit,
    goToDict: () -> Unit,
    currentScreenTitle: Int,
) {
    val index =
        when (currentScreenTitle) {
            R.string.write -> 1
            R.string.read -> 2
            R.string.dict -> 3
            else -> 0
        }
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        content = {
            NavigationBarItem(selected = index == 1, onClick = goToWrite, icon = {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = "navigate to writing page",
                )
            })
            NavigationBarItem(selected = index == 2, onClick = goToRead, icon = {
                Icon(
                    painter = painterResource(id = R.drawable.book_solid),
                    contentDescription = "navigate to reading page",
                )
            })

            NavigationBarItem(selected = index == 3, onClick = goToDict, icon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "navigate to rhyming dictionary page",
                )
            })
        },
    )
}
