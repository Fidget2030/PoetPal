package com.example.poetpal.ui.screens.writing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.poetpal.ui.screens.writing.components.SaveDialog
import com.example.poetpal.ui.utils.WritingField

@Suppress("ktlint:standard:function-naming")
@Composable
fun WritingScreen(
    writingViewModel: WritingViewModel =
        viewModel(factory = WritingViewModel.Factory),
) {
    val listState = rememberLazyListState()
    val snackBarHostState = remember { SnackbarHostState() }
    val writingState by writingViewModel.writeState.collectAsState()
    val poemState by writingViewModel.poemState.collectAsState()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { it ->
        if (writingState.newUnknown) {
            LaunchedEffect(key1 = writingState.unknownWords) {
                val state =
                    snackBarHostState.showSnackbar(
                        message = writingViewModel.snackBarMessage(),
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true,
                    )
                if (state == SnackbarResult.Dismissed) {
                    writingViewModel.toggleSnackBar()
                }
            }
        }
        if (writingState.showSaveDialog) {
            SaveDialog(
                title = poemState.title,
                author = poemState.author,
                onAuthorChanged = { writingViewModel.setAuthor(it) },
                onTitleChanged = { writingViewModel.setTitle(it) },
                onConfirmRequest = {
                    writingViewModel.saveLimerick()
                    writingViewModel.toggleSaveDialog()
                },
                onDismissRequest = { writingViewModel.toggleSaveDialog() },
            )
        }
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                state = listState,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                itemsIndexed(poemState.lines) { i, _ ->
                    WritingField(
                        poemState = poemState,
                        onValueChanged = writingViewModel::updateLine,
                        listState = listState,
                        index = i,
                    )
                }
            }

            Button(onClick = { writingViewModel.toggleSaveDialog() }) {
                Text("save")
            }
        }
    }
}
