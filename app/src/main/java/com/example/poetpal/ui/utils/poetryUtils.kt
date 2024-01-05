package com.example.poetpal.ui.utils

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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.poetpal.domain.Poem

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
@Composable
fun Poem(poem: Poem){
Poem(poem.text,poem.author)
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
