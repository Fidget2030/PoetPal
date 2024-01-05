package com.example.poetpal.ui.screens.reading

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.poetpal.ui.utils.Poem

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReadingScreen(readingViewModel: ReadingViewModel = viewModel(factory = ReadingViewModel.Factory)) {
    val metaState by readingViewModel.metaState.collectAsState()
    val selectedState by readingViewModel.selectedPoemState.collectAsState()

    val lazyListState = rememberLazyListState()
    Column {
        LazyColumn(state = lazyListState) {
            items(metaState.metas) {
                Row(modifier = Modifier.clickable { readingViewModel.getPoem(it.title) }) {
                    Text(text = it.title)
                    Text(text = it.author)
                    Text(text = it.type)
                }
            }
        }
        if (selectedState.selectedPoem != null) {
            Text(selectedState.selectedPoem!!.title)
            Poem(poem = selectedState.selectedPoem!!)
        }
    }
}
