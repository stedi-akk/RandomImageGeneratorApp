package com.stedi.randomimagegenerator.app.model.data.generatorparams;

import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;

import org.junit.Test;

import static junit.framework.Assert.*;

public class ColoredNoiseParamsTest {
    @Test
    public void testEquals() {
        ColoredNoiseParams params1 = new ColoredNoiseParams();
        ColoredNoiseParams params2 = new ColoredNoiseParams();
        params1.setNoiseType(ColoredNoiseGenerator.Type.TYPE_1);
        params1.setNoiseOrientation(ColoredNoiseGenerator.Orientation.HORIZONTAL);
        params2.setNoiseType(ColoredNoiseGenerator.Type.TYPE_1);
        params2.setNoiseOrientation(ColoredNoiseGenerator.Orientation.HORIZONTAL);

        assertTrue(params1.equals(params2));

        params1.setNoiseType(ColoredNoiseGenerator.Type.TYPE_6);
        assertFalse(params1.equals(params2));

        params1.setNoiseType(ColoredNoiseGenerator.Type.TYPE_1);
        params1.setNoiseOrientation(ColoredNoiseGenerator.Orientation.VERTICAL);
        assertFalse(params1.equals(params2));
    }

    @Test
    public void testValues() {
        ColoredNoiseParams params = new ColoredNoiseParams();

        assertNotNull(params.getNoiseOrientation());
        assertNotNull(params.getNoiseType());
        assertNotNull(params.createGenerator());

        params.setNoiseOrientation(ColoredNoiseGenerator.Orientation.VERTICAL);
        params.setNoiseType(ColoredNoiseGenerator.Type.TYPE_5);
        assertNotNull(params.createGenerator());
    }
}
