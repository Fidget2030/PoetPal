package com.example.poetpal.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.poetpal.ui.screens.PoetPalWelcome

@Suppress("ktlint:standard:function-naming")
@Composable
fun PoetPalApp(navController: NavHostController = rememberNavController())  {
    Scaffold(
        topBar = {},
        bottomBar = {},
        floatingActionButton = {},
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = PoetPalScreens.Welcome.name,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(route = PoetPalScreens.Welcome.name) {
                PoetPalWelcome()
            }
        }
    }
}
