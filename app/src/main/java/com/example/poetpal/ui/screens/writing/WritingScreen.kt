package com.example.poetpal.ui.screens.writing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.poetpal.ui.screens.writing.components.SaveDialog

@Suppress("ktlint:standard:function-naming")
@Composable
fun WritingScreen(
    writingViewModel: WritingViewModel =
        viewModel(factory = WritingViewModel.Factory),
) {
    val writingState by writingViewModel.writeState.collectAsState()
    val poemState by writingViewModel.poemState.collectAsState()
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
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = poemState.lines[0],
            onValueChange = { writingViewModel.updateLine(it, 0) },
            modifier = Modifier.padding(0.dp, 10.dp),
        )
        TextField(
            value = poemState.lines[1],
            onValueChange = { writingViewModel.updateLine(it, 1) },
            modifier = Modifier.padding(0.dp, 10.dp),
        )
        TextField(
            value = poemState.lines[2],
            onValueChange = { writingViewModel.updateLine(it, 2) },
            modifier = Modifier.padding(0.dp, 10.dp),
        )
        TextField(
            value = poemState.lines[3],
            onValueChange = { writingViewModel.updateLine(it, 3) },
            modifier = Modifier.padding(0.dp, 10.dp),
        )
        TextField(
            value = poemState.lines[4],
            onValueChange = { writingViewModel.updateLine(it, 4) },
            modifier = Modifier.padding(0.dp, 10.dp),
        )

        Button(onClick = { writingViewModel.toggleSaveDialog() }) {
            Text("save")
        }
    }
}
