package com.stedi.randomimagegenerator.app.model.data.generatorparams.base

import com.stedi.randomimagegenerator.app.model.data.SlowGenerator
import com.stedi.randomimagegenerator.generators.Generator

fun proxy(generator: Generator) = SlowGenerator(generator)