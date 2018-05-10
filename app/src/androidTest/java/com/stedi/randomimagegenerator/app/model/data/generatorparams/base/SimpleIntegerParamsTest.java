package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.generators.Generator;

import org.junit.Test;

import static junit.framework.Assert.*;

public class SimpleIntegerParamsTest {
    @SuppressLint("ParcelCreator")
    private class NonRandomParams extends SimpleIntegerParams {
        @Override
        public boolean canBeRandom() {
            return false;
        }

        @NonNull
        @Override
        public GeneratorType getType() {
            return null;
        }

        @NonNull
        @Override
        protected Generator createGenerator() {
            return null;
        }
    }

    @SuppressLint("ParcelCreator")
    private class RandomParams extends SimpleIntegerParams {
        @Override
        public boolean canBeRandom() {
            return true;
        }

        @NonNull
        @Override
        public GeneratorType getType() {
            return null;
        }

        @NonNull
        @Override
        protected Generator createGenerator() {
            return null;
        }
    }

    @Test
    public void testValues() {
        NonRandomParams nonRandomParams = new NonRandomParams();
        assertTrue(nonRandomParams.getValue() == 1);
        nonRandomParams.setValue(10);
        assertTrue(nonRandomParams.getValue() == 10);

        RandomParams randomParams = new RandomParams();
        assertNull(randomParams.getValue());
        randomParams.setValue(10);
        assertTrue(nonRandomParams.getValue() == 10);
    }

    @Test
    public void testEquals() {
        NonRandomParams nonRandomParams1 = new NonRandomParams();
        NonRandomParams nonRandomParams2 = new NonRandomParams();

        assertTrue(nonRandomParams1.equals(nonRandomParams2));
        nonRandomParams2.setValue(10);
        assertFalse(nonRandomParams1.equals(nonRandomParams2));

        RandomParams randomParams1 = new RandomParams();
        RandomParams randomParams2 = new RandomParams();

        assertTrue(randomParams1.equals(randomParams2));
        randomParams2.setValue(10);
        assertFalse(randomParams1.equals(randomParams2));
    }
}
