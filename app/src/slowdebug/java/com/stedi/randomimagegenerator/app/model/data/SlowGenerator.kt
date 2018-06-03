package com.stedi.randomimagegenerator.app.model.data

import android.graphics.Bitmap
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.app.GENERATOR_GENERATE_SLEEP
import com.stedi.randomimagegenerator.app.other.sleep
import com.stedi.randomimagegenerator.generators.Generator

class SlowGenerator(private val target: Generator) : Generator {

    override fun generate(imageParams: ImageParams): Bitmap? {
        sleep(GENERATOR_GENERATE_SLEEP)
        return target.generate(imageParams)
    }
}