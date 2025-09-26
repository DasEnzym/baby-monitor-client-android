package co.netguru.baby.monitor.client.feature.machinelearning

import android.content.Context
import co.netguru.baby.monitor.client.feature.voiceAnalysis.AacRecorder.Companion.SAMPLING_RATE
import io.reactivex.Single
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class MachineLearning(@Suppress("UNUSED_PARAMETER") context: Context) {

    fun processData(array: ShortArray) = Single.just(array).map { data ->
        val normalizedAmplitude = calculateNormalizedAmplitude(data)
        val cryingProbability = normalizedAmplitude.coerceIn(0f, 1f)
        val noiseProbability = 1f - cryingProbability

        mutableMapOf(
            OUTPUT_1_NOISE to noiseProbability,
            OUTPUT_2_CRYING_BABY to cryingProbability
        )
    }

    private fun calculateNormalizedAmplitude(data: ShortArray): Float {
        if (data.isEmpty()) {
            return 0f
        }

        val limitedData = if (data.size > DATA_SIZE) {
            data.copyOfRange(data.size - DATA_SIZE, data.size)
        } else {
            data
        }

        val rms = sqrt(
            limitedData.fold(0.0) { acc, sample ->
                val normalizedSample = sample.toDouble() / Short.MAX_VALUE
                acc + normalizedSample.pow(2.0)
            } / limitedData.size
        ).toFloat()

        val clippedRms = min(rms, 1f)
        return when {
            clippedRms <= BASELINE_NOISE_LEVEL -> 0f
            clippedRms >= MAX_EXPECTED_LEVEL -> 1f
            else -> (clippedRms - BASELINE_NOISE_LEVEL) /
                (MAX_EXPECTED_LEVEL - BASELINE_NOISE_LEVEL)
        }
    }

    companion object {
        internal const val DATA_SIZE = 4 * SAMPLING_RATE
        const val OUTPUT_1_NOISE = "NOISE"
        const val OUTPUT_2_CRYING_BABY = "CRYING_BABY"

        const val CRYING_THRESHOLD = 0.7

        private const val BASELINE_NOISE_LEVEL = 0.1f
        private const val MAX_EXPECTED_LEVEL = 0.8f
    }
}
