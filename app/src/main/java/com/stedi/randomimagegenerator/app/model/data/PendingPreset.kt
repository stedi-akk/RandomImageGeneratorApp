package com.stedi.randomimagegenerator.app.model.data

import android.os.Bundle
import com.stedi.randomimagegenerator.Quality
import com.stedi.randomimagegenerator.app.di.RootSavePath
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.app.other.logger.Logger
import java.io.File

class PendingPreset(
        private val unsavedName: String,
        @RootSavePath
        private val rootSavePath: String,
        private val logger: Logger) {

    companion object {
        private val KEY_MAIN_PRESET = "KEY_MAIN_PRESET"
        private val KEY_CANDIDATE_FROM_PRESET = "KEY_CANDIDATE_FROM_PRESET"
        private val KEY_CANDIDATE_PRESET = "KEY_CANDIDATE_PRESET"
    }

    private var preset: Preset? = null
    private var candidateFrom: Preset? = null
    private var candidate: Preset? = null

    fun get() = preset

    fun newDefaultCandidate() {
        candidateFrom = null
        candidate = Preset(
                unsavedName,
                GeneratorParams.createDefaultParams(GeneratorType.COLORED_CIRCLES),
                Quality.png(),
                rootSavePath + File.separator + "0")
                .apply {
                    setWidth(800)
                    setHeight(800)
                    setCount(5)
                }
        logger.log(this, "after newDefaultCandidate: $this")
    }

    fun prepareCandidateFrom(candidate: Preset) {
        if (candidate === preset) {
            this.candidateFrom = null
            this.candidate = candidate
        } else {
            this.candidateFrom = candidate
            this.candidate = candidate.createCopy()
        }
        logger.log(this, "after prepareCandidateFrom: $this")
    }

    fun candidateSaved() {
        prepareCandidateFrom(candidate ?: throw IllegalStateException("candidate is null"))
    }

    fun getCandidate() = candidate

    fun isCandidateNewOrChanged() = !(candidate?.equals(candidateFrom)
            ?: throw IllegalStateException("candidate is null"))

    fun applyCandidate() {
        val candidate = candidate ?: throw IllegalStateException("candidate is null")
        if (candidateFrom != null) {
            candidate.clearIds()
            candidate.name = unsavedName
        }
        candidate.timestamp = System.currentTimeMillis()
        preset = candidate
        logger.log(this, "after applyCandidate: $this")
    }

    fun killCandidate() {
        candidateFrom = null
        candidate = null
        logger.log(this, "after killCandidate: $this")
    }

    fun clear() {
        preset = null
        logger.log(this, "after clear: $this")
    }

    fun retain(bundle: Bundle) {
        bundle.putParcelable(KEY_MAIN_PRESET, preset)
        bundle.putParcelable(KEY_CANDIDATE_FROM_PRESET, candidateFrom)
        bundle.putParcelable(KEY_CANDIDATE_PRESET, candidate)
        logger.log(this, "after retain: $this")
    }

    fun restore(bundle: Bundle) {
        preset = bundle.getParcelable(KEY_MAIN_PRESET)
        candidateFrom = bundle.getParcelable(KEY_CANDIDATE_FROM_PRESET)
        candidate = bundle.getParcelable(KEY_CANDIDATE_PRESET)
        logger.log(this, "after restore: $this")
    }

    override fun toString() = "PendingPreset{" +
            "preset=$preset" +
            ", candidateFrom=$candidateFrom" +
            ", candidate=$candidate" +
            '}'
}