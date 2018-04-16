package com.stedi.randomimagegenerator.app.model.data

import android.os.Parcelable
import com.stedi.randomimagegenerator.Quality
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.app.other.logger.Logger
import javax.inject.Singleton

@Singleton
class PendingPreset(private val logger: Logger) {

    private var preset: Preset? = null
    private var candidateFrom: Preset? = null
    private var candidate: Preset? = null

    fun newDefaultCandidate() {
        candidateFrom = null
        // name and save path are set in ApplyGenerationPresenterImpl
        candidate = Preset("", GeneratorParams.createDefaultParams(GeneratorType.COLORED_CIRCLES), Quality.png(), "")
                .apply {
                    setWidth(800)
                    setHeight(800)
                    setCount(5)
                }
        logger.log(this, "after newDefaultCandidate: $this")
    }

    fun prepareCandidateFrom(preset: Preset) {
        if (preset === this.preset) {
            this.candidateFrom = null
            this.candidate = preset
        } else {
            this.candidateFrom = preset
            this.candidate = preset.makeCopy()
        }
        logger.log(this, "after prepareCandidateFrom: $this")
    }

    fun getPreset() = preset

    fun getCandidate() = candidate

    fun isCandidateNew() = candidateFrom == null && candidate != null

    fun isCandidateChanged() = candidateFrom != null && !(candidate?.equals(candidateFrom) ?: true)

    fun applyCandidate() {
        preset = candidate
        logger.log(this, "after applyCandidate: $this")
    }

    fun clearCandidate() {
        candidateFrom = null
        candidate = null
        logger.log(this, "after clearCandidate: $this")
    }

    fun clearPreset() {
        preset = null
        logger.log(this, "after clearPreset: $this")
    }

    fun retain(): Array<Parcelable?> = arrayOf(preset, candidateFrom, candidate)

    fun restore(state: Array<Parcelable?>) {
        preset = state[0] as Preset?
        candidateFrom = state[1] as Preset?
        candidate = state[2] as Preset?
    }

    override fun toString() = "PendingPreset{" +
            "preset=$preset" +
            ", candidateFrom=$candidateFrom" +
            ", candidate=$candidate" +
            '}'
}