package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;

public class GeneratorParamsCreateDefaultTest {
    @Test
    public void testCreateDefaultParams() {
        for (GeneratorType gt : GeneratorType.Companion.getNON_EFFECT_TYPES()) {
            GeneratorParams gp = GeneratorParams.Companion.createDefaultParams(gt);
            assertNotNull(gp);
        }
    }

    @Test
    public void testCreateDefaultParamsFail() {
        for (GeneratorType gt : GeneratorType.Companion.getEFFECT_TYPES()) {
            try {
                GeneratorParams.Companion.createDefaultParams(gt);
                fail();
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testCreateDefaultEffectParams() {
        GeneratorParams target = GeneratorParams.Companion.createRandomDefaultParams();
        for (GeneratorType gt : GeneratorType.Companion.getEFFECT_TYPES()) {
            GeneratorParams gp = GeneratorParams.Companion.createDefaultEffectParams(gt, target);
            assertNotNull(gp);
        }
    }

    @Test
    public void testCreateDefaultEffectParamsFail() {
        GeneratorParams target = GeneratorParams.Companion.createRandomDefaultParams();
        for (GeneratorType gt : GeneratorType.Companion.getNON_EFFECT_TYPES()) {
            try {
                GeneratorParams.Companion.createDefaultEffectParams(gt, target);
                fail();
            } catch (IllegalArgumentException e) {
            }
        }
    }
}
