package com.stedi.randomimagegenerator.app.model.data;

import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredNoiseParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PresetTest {
    private static final List<Quality> qualities = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() {
        qualities.add(Quality.png());
        qualities.add(Quality.jpg(100));
        qualities.add(new Quality(Bitmap.CompressFormat.WEBP, 100));
    }

    @Test
    public void testCreateCopyAndEquals() {
        ColoredNoiseParams generatorParams = (ColoredNoiseParams) GeneratorParams.createDefaultParams(GeneratorType.COLORED_NOISE);
        Preset preset = new Preset("ololo", generatorParams, Quality.png(), "path");

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
        assertTrue(copy.getPathToSave().equals("path"));
        assertNull(copy.getWidthRange());
        assertNull(copy.getHeightRange());

        assertTrue(copy.equals(preset));

        generatorParams.setNoiseOrientation(ColoredNoiseGenerator.Orientation.VERTICAL);
        generatorParams.setNoiseType(ColoredNoiseGenerator.Type.TYPE_6);
        assertFalse(copy.equals(preset));
    }

    @Test
    public void testCreateCopyAndEqualsNonEffectParams() {
        int ids = 1;
        for (GeneratorType generatorType : GeneratorType.nonEffectTypes()) {
            for (Quality quality : qualities) {
                GeneratorParams generatorParams = GeneratorParams.createDefaultParams(generatorType);
                String name = "name" + ids;
                Preset preset = new Preset(name, generatorParams, quality, "path");

                preset.setId(ids);
                long timestamp = System.currentTimeMillis();
                preset.setTimestamp(timestamp);
                preset.setWidth(100);
                preset.setHeightRange(10, 100, 10);

                Preset copy = preset.createCopy();

                assertTrue(copy.getId() == ids);
                assertTrue(copy.getTimestamp() == timestamp);
                assertTrue(copy.getName().equals(name));
                assertTrue(copy.getGeneratorParams().equals(generatorParams));
                assertTrue(copy.getWidth() == 100);
                assertArrayEquals(copy.getHeightRange(), new int[]{10, 100, 10});
                assertTrue(copy.getQuality().getFormat() == quality.getFormat());
                assertTrue(copy.getQuality().getQualityValue() == quality.getQualityValue());
                assertTrue(copy.getPathToSave().equals("path"));
                assertNull(copy.getWidthRange());

                assertTrue(copy.equals(preset));

                ids++;
            }
        }
    }

    @Test
    public void testCreateCopyAndEqualsEffectParams() {
        int ids = 1;
        for (GeneratorType effectType : GeneratorType.effectTypes()) {
            for (GeneratorType nonEffectType : GeneratorType.nonEffectTypes()) {
                for (Quality quality : qualities) {
                    GeneratorParams generatorParams = GeneratorParams.createDefaultEffectParams(effectType, GeneratorParams.createDefaultParams(nonEffectType));
                    String name = "name" + ids;
                    Preset preset = new Preset(name, generatorParams, quality, "path");

                    preset.setId(ids);
                    long timestamp = System.currentTimeMillis();
                    preset.setTimestamp(timestamp);
                    preset.setWidthRange(100, 10, 10);
                    preset.setHeightRange(10, 1000, 10);

                    Preset copy = preset.createCopy();

                    assertTrue(copy.getId() == ids);
                    assertTrue(copy.getTimestamp() == timestamp);
                    assertTrue(copy.getName().equals(name));
                    assertTrue(copy.getGeneratorParams().equals(generatorParams));
                    assertArrayEquals(copy.getWidthRange(), new int[]{100, 10, 10});
                    assertArrayEquals(copy.getHeightRange(), new int[]{10, 1000, 10});
                    assertTrue(copy.getQuality().getFormat() == quality.getFormat());
                    assertTrue(copy.getQuality().getQualityValue() == quality.getQualityValue());
                    assertTrue(copy.getPathToSave().equals("path"));

                    assertTrue(copy.equals(preset));

                    ids++;
                }
            }
        }
    }

    @Test
    public void testSettersExceptions() {
        Preset preset = new Preset("ololo", GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR), Quality.png(), "path");
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
            preset.setPathToSave("");
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
