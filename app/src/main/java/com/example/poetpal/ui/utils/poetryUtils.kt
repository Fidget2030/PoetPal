package com.example.poetpal.ui.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.poetpal.domain.Poem
import com.example.poetpal.ui.screens.writing.PoemState
import kotlinx.coroutines.launch

val aMeter =
    listOf(
        listOf("(u)"),
        listOf("u"),
        listOf("/"),
        listOf("u"),
        listOf("u"),
        listOf("/"),
        listOf("u"),
        listOf("u"),
        listOf("/"),
        listOf("(u)"),
        listOf("(u)"),
    )
val bMeter =
    listOf(
        listOf("(u)"),
        listOf("u"),
        listOf("/"),
        listOf("u"),
        listOf("u"),
        listOf("/"),
        listOf("(u)"),
    )

@Suppress("ktlint:standard:function-naming")
fun limerick(
    lines: List<String>,
    meter: List<String>,
    extraMeter: List<String>,
    rhymes: List<String>,
): AnnotatedString {
    checkLimerickLines(meter.size, lines.size, rhymes.size, extraMeter.size)
    checkLimerickMeter(meter)
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
    return result
}

fun checkLimerickLines(
    meterSize: Int,
    lineSize: Int,
    rhymeSize: Int,
    extraMeterSize: Int,
) {
    var incorrectSizes = ""
    when {
        meterSize != 5 -> incorrectSizes += "meter, "
        lineSize != 5 -> incorrectSizes += "lines, "
        rhymeSize != 5 -> incorrectSizes += "rhyme, "
        extraMeterSize != 5 -> incorrectSizes += "meter variances, "
    }
    if (incorrectSizes.isNotEmpty()) {
        incorrectSizes = incorrectSizes.dropLast(2)
        throw IllegalArgumentException("$incorrectSizes must have 5 lines")
    }
}

fun checkLimerickMeter(meter: List<String>) {
    var incorrectMeters = ""
    when {
        meter[0].replace(" ", "") != "u/uu/uu/" -> incorrectMeters += "1, "
        meter[1].replace(" ", "") != "u/uu/uu/" -> incorrectMeters += "2, "
        meter[2].replace(" ", "") != "u/uu/" -> incorrectMeters += "3, "
        meter[3].replace(" ", "") != "u/uu/" -> incorrectMeters += "4, "
        meter[4].replace(" ", "") != "u/uu/uu/" -> incorrectMeters += "5, "
    }
    if (incorrectMeters.isNotEmpty()) {
        incorrectMeters.dropLast(2)
        throw java.lang.IllegalArgumentException("The meter in lines $incorrectMeters is incorrect.")
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun Poem(poem: Poem) {
    Poem(poem.text, poem.author)
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

@Suppress("ktlint:standard:function-naming")
@Composable
fun WritingField(
    onValueChanged: (String, Int) -> Unit,
    listState: LazyListState,
    index: Int,
    poemState: PoemState,
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    Text(text = poemState.annotatedLines[index])
    OutlinedTextField(
        value = poemState.lines[index],
        onValueChange = { onValueChanged(it, index) },
        modifier = Modifier.padding(0.dp, 10.dp),
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions =
            KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                    coroutineScope.launch {
                        listState.scrollToItem(index + 1)
                    }
                },
            ),
    )
}
