package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.generators.Generator;

import org.junit.Test;

import static junit.framework.Assert.*;

public class EffectGeneratorParamsEqualsTest {
    @SuppressLint("ParcelCreator")
    private class EffectParams1 extends EffectGeneratorParams {
        public EffectParams1(@NonNull GeneratorParams target) {
            super(target);
        }

        @NonNull
        @Override
        protected Generator createEffectGenerator(@NonNull com.stedi.randomimagegenerator.generators.Generator target) {
            return null;
        }

        @Override
        public void setId(int id) {

        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public boolean isEditable() {
            return false;
        }

        @NonNull
        @Override
        public GeneratorType getType() {
            return null;
        }
    }

    @SuppressLint("ParcelCreator")
    private class EffectParams2 extends EffectParams1 {
        public EffectParams2(@NonNull GeneratorParams target) {
            super(target);
        }
    }

    @Test
    public void test() {
        GeneratorParams target1 = GeneratorParams.Companion.createDefaultParams(GeneratorType.FLAT_COLOR);
        GeneratorParams target2 = GeneratorParams.Companion.createDefaultParams(GeneratorType.COLORED_PIXELS);

        EffectParams1 effectParams1First = new EffectParams1(target1);
        EffectParams1 effectParams1Second = new EffectParams1(target1);

        assertTrue(effectParams1First.equals(effectParams1Second));
        effectParams1Second = new EffectParams1(target2);
        assertFalse(effectParams1First.equals(effectParams1Second));

        EffectParams2 effectParams2First = new EffectParams2(target1);
        EffectParams2 effectParams2Second = new EffectParams2(target1);

        assertTrue(effectParams2First.equals(effectParams2Second));
        effectParams2Second = new EffectParams2(target2);
        assertFalse(effectParams2First.equals(effectParams2Second));

        assertFalse(effectParams1First.equals(effectParams2First));
        assertFalse(effectParams2Second.equals(effectParams1Second));
    }
}
