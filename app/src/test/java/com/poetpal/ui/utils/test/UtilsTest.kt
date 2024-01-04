package com.poetpal.ui.utils.test

import com.example.poetpal.ui.utils.limerick
import org.junit.Assert.assertThrows
import org.junit.Assert.fail
import org.junit.Test

class UtilsTest {
    private val lines =
        listOf(
            "The lim'rick packs laughs anat",
            "Into space that is quite econ",
            "But the good ones I've s",
            "So seldom are cl",
            "And the clean ones so seldom are c",
        )
    private val meter =
        listOf(
            " u   /   u    u      /    u u /",
            "  u   /    u    u   /   u u /",
            "   u   /    u    u    /",
            " u  /  u   u    /",
            "   u    /    u    u  /  u   u   /",
        )
    private val meterVariance = listOf(", u u", "u, u u", " u,", ",", " u, u u")
    private val rhymes = listOf("omical", "omical", "een", "ean", "omical")

    @Test
    fun createsAnnotatedLimerick_withCorrectParameters() {
        try {
            (limerick(lines = lines, meter = meter, extraMeter = meterVariance, rhymes = rhymes))
        } catch (e: Exception) {
            fail("should not have thrown any errors")
        }
    }

    @Test
    fun throwsIllegalArgumentException_WithIncorrectSizedParameters() {
        assertThrows("lines must have 5 lines", IllegalArgumentException::class.java) {
            limerick(
                lines = lines.subList(0, 3),
                meter = meter,
                extraMeter = meterVariance,
                rhymes = rhymes,
            )
        }
        assertThrows("meter must have 5 lines", IllegalArgumentException::class.java) {
            limerick(
                lines = lines,
                meter = meter.subList(0, 3),
                extraMeter = meterVariance,
                rhymes = rhymes,
            )
        }
        assertThrows("meter variance must have 5 lines", IllegalArgumentException::class.java) {
            limerick(
                lines = lines,
                meter = meter,
                extraMeter = meterVariance.subList(0, 3),
                rhymes = rhymes,
            )
        }
        assertThrows("rhymes must have 5 lines", IllegalArgumentException::class.java) {
            limerick(
                lines = lines,
                meter = meter,
                extraMeter = meterVariance,
                rhymes = rhymes.subList(0, 3),
            )
        }
        assertThrows("meter,rhymes must have 5 lines", IllegalArgumentException::class.java) {
            limerick(
                lines = lines,
                meter = meter.subList(0, 3),
                extraMeter = meterVariance,
                rhymes = rhymes.subList(0, 3),
            )
        }
    }

    @Test
    fun throwsIllegalArgumentException_WithIncorrectMeter() {
        val incorrectmeter = meter.toMutableList()
        incorrectmeter[0] = " u   /   u    u      /    u    "
        incorrectmeter[3] = incorrectmeter[4]
        assertThrows("the meter in lines 1, 4 is incorrect", IllegalArgumentException::class.java) {
            limerick(
                lines = lines,
                meter = incorrectmeter,
                extraMeter = meterVariance,
                rhymes = rhymes,
            )
        }
    }
}
