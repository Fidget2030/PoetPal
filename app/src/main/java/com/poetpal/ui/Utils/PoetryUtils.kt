package com.poetpal.ui.Utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Suppress("ktlint:standard:function-naming")
@Composable
fun Limerick(
    lines: List<String>,
    meter: List<String>,
    extraMeter: List<String>,
    rhymes: List<String>,
) {
    var i = 0
    var result: AnnotatedString = buildAnnotatedString { append("") }
    while (i < 5) {
        val extras = extraMeter[i].split(",")
        result +=
            buildAnnotatedString {
                append("   ")
                withStyle(SpanStyle(color = Color.LightGray)) { append(extras[0]) }
                append(meter[i])
                withStyle(SpanStyle(color = Color.LightGray)) { append(extras[1]) }
                append("\n")
                append("${i + 1}. ")
                append(lines[i])
                if (i == 2 || i == 3) {
                    withStyle(SpanStyle(color = Color.Red)) {
                        append(rhymes[i])
                    }
                } else {
                    withStyle(SpanStyle(color = Color.Blue)) {
                        append(rhymes[i])
                    }
                }
                append("\n\n")
            }
        i++
    }
    Text(result, style = TextStyle(fontFamily = FontFamily.Monospace))
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun Poem(
    text: String,
    author: String,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Text(
            text =
                text.trimMargin(),
            textAlign = TextAlign.Center,
        )
        Text(
            text = "- $author -".trimMargin(),
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
        )
    }
}
