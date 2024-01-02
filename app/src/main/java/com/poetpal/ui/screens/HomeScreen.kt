package com.poetpal.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.poetpal.ui.Utils.Limerick
import com.poetpal.ui.Utils.Poem
import com.poetpal.ui.theme.PoetPalTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeScreen(
    goToWrite: () -> Unit,
    goToRead: () -> Unit,
) {
    // future plan: add tabs for different types of poems: sonnets, haiku's,...
    val lines =
        listOf(
            "The lim'rick packs laughs anat",
            "Into space that is quite econ",
            "But the good ones I've s",
            "So seldom are cl",
            "And the clean ones so seldom are c",
        )
    val meter =
        listOf(
            " u   /   u    u      /    u u /",
            "  u   /    u    u   /   u u /",
            "   u   /    u    u    /",
            " u  /  u   u    /",
            "   u    /    u    u  /  u   u   /",
        )
    val meterVariance = listOf(", u u", "u, u u", " u,", ",", " u, u u")
    val rhymes = listOf("omical", "omical", "een", "ean", "omical")

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Poem(
            text = """The limerick packs laughs anatomical
                Into space that is quite economical.
                But the good ones I've seen
                So seldom are clean
                And the clean ones so seldom are comical.""",
            author = "Anonymous",
        )

        Spacer(modifier = Modifier.padding(20.dp))

        Text(
            text =
"""A limerick has 5 lines.
The lines rhyme according to an AABBA scheme.
Lines 1,2 and 5 are an anapestic trimeter,
lines 3 and 4 are an anapestic dimeter
slight variation is allowed
""".trimMargin(),
        )
        Spacer(modifier = Modifier.padding(20.dp))

        Limerick(lines = lines, meter = meter, extraMeter = meterVariance, rhymes = rhymes)
        Button(onClick = goToWrite) {
            Text(text = "Write your own!") //
        }
        Button(onClick = goToRead) {
            Text(text = "Show me some more examples!") //
        }
    }
}

@Preview(showBackground = true)
@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeScreenPreview() {
    PoetPalTheme { Surface { HomeScreen({}, {}) } }
}
