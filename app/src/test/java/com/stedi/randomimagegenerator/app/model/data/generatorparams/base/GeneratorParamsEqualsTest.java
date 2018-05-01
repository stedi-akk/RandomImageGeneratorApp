package com.stedi.randomimagegenerator.app.model.data.generatorparams.base;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.generators.Generator;

import org.junit.Test;

import static junit.framework.Assert.*;

public class GeneratorParamsEqualsTest {
    @SuppressLint("ParcelCreator")
    private class GeneratorParams1 extends GeneratorParams {

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

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }

    @SuppressLint("ParcelCreator")
    private class GeneratorParams2 extends GeneratorParams1 {
    }

    @Test
    public void test() {
        GeneratorParams1 gp1First = new GeneratorParams1();
        GeneratorParams1 gp1Second = new GeneratorParams1();

        assertTrue(gp1First.equals(gp1Second));

        GeneratorParams2 gp2First = new GeneratorParams2();
        GeneratorParams2 gp2Second = new GeneratorParams2();

        assertTrue(gp2First.equals(gp2Second));

        assertFalse(gp1First.equals(gp2First));
        assertFalse(gp2Second.equals(gp1Second));
    }
}
