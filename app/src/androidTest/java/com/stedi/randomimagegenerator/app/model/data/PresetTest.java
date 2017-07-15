package com.stedi.randomimagegenerator.app.model.data;

import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PresetTest {
    private GeneratorParams generatorParams;
    private Preset preset;

    @Before
    public void before() {
        generatorParams = GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR);
        preset = new Preset("ololo",
                generatorParams,
                Quality.png(),
                "null");
    }

    @Test
    public void testCreateCopyAndEquals() {
        preset.setId(1);
        preset.setTimestamp(1337L);
        preset.setWidth(100);
        preset.setHeight(300);
        preset.setCount(666);

        Preset copy = preset.createCopy();

        assertTrue(copy.getId() == 1);
        assertTrue(copy.getTimestamp() == 1337L);
        assertTrue(copy.getName().equals("ololo"));
        assertTrue(copy.getGeneratorParams().equals(generatorParams));
        assertTrue(copy.getWidth() == 100);
        assertTrue(copy.getHeight() == 300);
        assertTrue(copy.getCount() == 666);
        assertTrue(copy.getQuality().getFormat() == Bitmap.CompressFormat.PNG);
        assertTrue(copy.getQuality().getQualityValue() == 100);
        assertTrue(copy.getSaveFolder().equals("null"));
        assertNull(copy.getWidthRange());
        assertNull(copy.getHeightRange());

        assertTrue(copy.equals(preset));

        copy.setName("trololo");
        assertFalse(copy.equals(preset));
    }

    @Test
    public void testSettersExceptions() {
        try {
            preset.setId(0);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            preset.setTimestamp(0);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            preset.setName("");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            preset.setWidth(-1);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            preset.setHeight(-1);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            preset.setWidthRange(-1, -10, -100);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            preset.setHeightRange(-1, -10, -100);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            preset.setCount(-10);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            preset.setSaveFolder("");
            fail();
        } catch (IllegalArgumentException e) {
        }
        preset.setWidthRange(10, 100, 10);
        preset.setHeightRange(10, 100, 10);
        try {
            preset.setCount(10);
            fail();
        } catch (IllegalStateException e) {
        }
    }
}
