package com.example.poetpal.ui.screens.writing.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Suppress("ktlint:standard:function-naming")
@Composable
fun SaveDialog(
    author: String,
    title: String,
    onAuthorChanged: (String) -> Unit,
    onTitleChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Text(
                "Save",
                modifier = Modifier.wrapContentSize(Alignment.TopCenter),
                textAlign = TextAlign.Center,
            )
            Row {
                Text("Title", modifier = Modifier.padding(0.dp, 0.dp, 15.dp, 10.dp))
                TextField(value = title, onValueChange = onTitleChanged)
            }
            Row {
                Text("Author", modifier = Modifier.padding(0.dp, 0.dp, 15.dp, 10.dp))
                TextField(value = author, onValueChange = onAuthorChanged)
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Row {
                Button(onClick = { onConfirmRequest() }) {
                    Text(text = "Save")
                }
                Button(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}
