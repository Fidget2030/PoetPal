package com.example.poetpal.ui.screens.writing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun WritingScreen(
    writingViewModel: WritingViewModel =
        viewModel(factory = WritingViewModel.Factory),
) {
    val writingState by writingViewModel.writeState.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(value = writingState.lines[0], onValueChange = { writingViewModel.updateLine(it, 0) })
        TextField(value = writingState.lines[1], onValueChange = { writingViewModel.updateLine(it, 1) })
        TextField(value = writingState.lines[2], onValueChange = { writingViewModel.updateLine(it, 2) })
        TextField(value = writingState.lines[3], onValueChange = { writingViewModel.updateLine(it, 3) })
        TextField(value = writingState.lines[4], onValueChange = { writingViewModel.updateLine(it, 4) })

        Button(onClick = { writingViewModel.saveLimerick() }) {
        }
    }
}
