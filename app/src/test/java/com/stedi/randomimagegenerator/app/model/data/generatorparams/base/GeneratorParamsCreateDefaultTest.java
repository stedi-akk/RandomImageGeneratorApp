package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

public class GeneratorParamsCreateDefaultTest {
    private static List<GeneratorType> nonEffectTypes = new ArrayList<>();
    private static List<GeneratorType> effectTypes = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() {
        for (GeneratorType gt : GeneratorType.values()) {
            if (gt.isEffect()) {
                effectTypes.add(gt);
            } else {
                nonEffectTypes.add(gt);
            }
        }
    }

    @Test
    public void testCreateDefaultParams() {
        for (GeneratorType gt : nonEffectTypes) {
            GeneratorParams gp = GeneratorParams.createDefaultParams(gt);
            assertNotNull(gp);
        }
    }

    @Test
    public void testCreateDefaultParamsFail() {
        for (GeneratorType gt : effectTypes) {
            try {
                GeneratorParams.createDefaultParams(gt);
                fail();
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testCreateDefaultEffectParams() {
        GeneratorParams target = GeneratorParams.createDefaultParams(nonEffectTypes.get(0));
        for (GeneratorType gt : effectTypes) {
            GeneratorParams gp = GeneratorParams.createDefaultEffectParams(gt, target);
            assertNotNull(gp);
        }
    }

    @Test
    public void testCreateDefaultEffectParamsFail() {
        GeneratorParams target = GeneratorParams.createDefaultParams(nonEffectTypes.get(0));
        for (GeneratorType gt : nonEffectTypes) {
            try {
                GeneratorParams.createDefaultEffectParams(gt, target);
                fail();
            } catch (IllegalArgumentException e) {
            }
        }
    }
}
