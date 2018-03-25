package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;

import org.junit.Test;

import static junit.framework.Assert.*;

public class GeneratorParamsCreateDefaultTest {
    @Test
    public void testCreateDefaultParams() {
        for (GeneratorType gt : GeneratorType.nonEffectTypes()) {
            GeneratorParams gp = GeneratorParams.Companion.createDefaultParams(gt);
            assertNotNull(gp);
        }
    }

    @Test
    public void testCreateDefaultParamsFail() {
        for (GeneratorType gt : GeneratorType.effectTypes()) {
            try {
                GeneratorParams.Companion.createDefaultParams(gt);
                fail();
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testCreateDefaultEffectParams() {
        GeneratorParams target = GeneratorParams.Companion.createDefaultParams(GeneratorType.nonEffectTypes()[0]);
        for (GeneratorType gt : GeneratorType.effectTypes()) {
            GeneratorParams gp = GeneratorParams.Companion.createDefaultEffectParams(gt, target);
            assertNotNull(gp);
        }
    }

    @Test
    public void testCreateDefaultEffectParamsFail() {
        GeneratorParams target = GeneratorParams.Companion.createDefaultParams(GeneratorType.nonEffectTypes()[0]);
        for (GeneratorType gt : GeneratorType.nonEffectTypes()) {
            try {
                GeneratorParams.Companion.createDefaultEffectParams(gt, target);
                fail();
            } catch (IllegalArgumentException e) {
            }
        }
    }
}
