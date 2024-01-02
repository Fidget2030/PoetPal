package com.poetpal.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.poetpal.navigation.PoetPalScreens
import com.poetpal.navigation.navBarComposables.PoetBottomBar
import com.poetpal.navigation.navBarComposables.PoetTopBar
import com.poetpal.ui.screens.AboutSidePanel
import com.poetpal.ui.screens.DictionaryScreen
import com.poetpal.ui.screens.HomeScreen
import com.poetpal.ui.screens.ReadingScreen
import com.poetpal.ui.screens.WritingScreen
import com.poetpal.ui.theme.PoetPalTheme
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@Composable
fun PoetPalApp(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val scope = rememberCoroutineScope()
    val canNavigateBack = navController.previousBackStackEntry != null
    val navigateUp: () -> Unit = { navController.navigateUp() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val goToWrite: () -> Unit = {
        navController.navigate(PoetPalScreens.Write.name) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
    val goToRead: () -> Unit = {
        navController.navigate(PoetPalScreens.Read.name) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
    val goToDict: () -> Unit = {
        navController.navigate(PoetPalScreens.Dictionary.name) {
            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }
    val currentScreenTitle =
        PoetPalScreens.valueOf(
            backStackEntry?.destination?.route ?: PoetPalScreens.Home.name,
        ).title
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            AboutSidePanel {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }
        }
    }, gesturesEnabled = false) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                PoetTopBar(
                    canNavigateBack = canNavigateBack,
                    navigateUp = navigateUp,
                    currentScreenTitle = currentScreenTitle,
                    toggleSidePanel = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                )
            },
            bottomBar = { PoetBottomBar(goToWrite, goToRead, goToDict, currentScreenTitle) },
            floatingActionButton = {},
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = PoetPalScreens.Home.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(route = PoetPalScreens.Home.name) {
                    HomeScreen(goToWrite, goToRead)
                }
                composable(route = PoetPalScreens.Read.name) {
                    ReadingScreen()
                }
                composable(route = PoetPalScreens.Write.name) {
                    WritingScreen()
                }
                composable(route = PoetPalScreens.Dictionary.name) {
                    DictionaryScreen()
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun PoetAppPreview() {
    PoetPalTheme {
        Surface(modifier = Modifier.fillMaxWidth()) {
            PoetPalApp()
        }
    }
}
