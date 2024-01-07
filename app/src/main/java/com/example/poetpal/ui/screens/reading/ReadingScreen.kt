package com.example.poetpal.ui.screens.reading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.poetpal.ui.utils.Poem
import com.example.poetpal.ui.utils.VerticalDivider

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ReadingScreen(readingViewModel: ReadingViewModel = viewModel(factory = ReadingViewModel.Factory)) {
    val metaState by readingViewModel.metaState.collectAsState()
    val selectedState by readingViewModel.selectedPoemState.collectAsState()

    val lazyListState = rememberLazyListState()
    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer),
        ) {
            Text(
                text = "Title",
                modifier = Modifier.weight(2f).padding(bottom = 5.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            VerticalDivider(
                modifier =
                    Modifier
                        .height(27.dp)
                        .padding(8.dp, 0.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = "Author",
                modifier = Modifier.weight(2f).padding(bottom = 5.dp),
                style = MaterialTheme.typography.titleMedium,
            )
            VerticalDivider(
                modifier =
                    Modifier
                        .height(27.dp)
                        .padding(8.dp, 0.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = "Type",
                modifier = Modifier.weight(1f).padding(bottom = 5.dp),
                style = MaterialTheme.typography.titleMedium,
            )
        }
        LazyColumn(state = lazyListState, modifier = Modifier.fillMaxHeight(0.55f)) {
            items(metaState.metas, contentType = { it.type }) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable { readingViewModel.getPoem(it.title) },
                ) {
                    Box(modifier = Modifier.weight(2f)) {
                        PlainTooltipBox(
                            tooltip = { Text(text = it.title) },
                        ) {
                            Text(
                                text = it.title,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                modifier =
                                    Modifier
                                        .tooltipAnchor().padding(bottom = 5.dp),
                            )
                        }
                    }
                    VerticalDivider(
                        modifier =
                            Modifier
                                .height(27.dp)
                                .padding(8.dp, 0.dp),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                    )
                    Box(modifier = Modifier.weight(2f)) {
                        PlainTooltipBox(
                            tooltip = { Text(text = it.author) },
                        ) {
                            Text(
                                text = it.author,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                modifier =
                                    Modifier
                                        .tooltipAnchor().padding(bottom = 5.dp),
                            )
                        }
                    }
                    VerticalDivider(
                        modifier =
                            Modifier
                                .height(27.dp)
                                .padding(8.dp, 0.dp),
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                    )

                    Text(
                        text = it.type,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier =
                            Modifier.weight(
                                1f,
                            ).padding(bottom = 5.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        if (selectedState.selectedPoem != null) {
            Poem(poem = selectedState.selectedPoem!!)
        }
    }
}
