package com.stedi.randomimagegenerator.app.model.data;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PendingPresetTest {
    private PendingPreset pendingPreset;

    @Before
    public void before() {
        pendingPreset = new PendingPreset(new SoutLogger("PendingPresetTest"));
    }

    @Test
    public void testEmpty() {
        assertNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateChanged());
        assertFalse(pendingPreset.isCandidateNew());
    }

    @Test
    public void testNewDefaultCandidate() {
        pendingPreset.newDefaultCandidate();
        assertNull(pendingPreset.getPreset());
        assertNotNull(pendingPreset.getCandidate());
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        String prevName = pendingPreset.getCandidate().getName();
        pendingPreset.getCandidate().setName("changed");
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());
        pendingPreset.getCandidate().setName(prevName);
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        pendingPreset.applyCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertTrue(pendingPreset.getPreset().equals(pendingPreset.getCandidate()));
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        pendingPreset.clearCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        pendingPreset.clearPreset();
        assertNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());
    }

    @Test
    public void testPrepareCandidateFrom() {
        Preset from = new Preset("razdwatri",
                GeneratorParams.Companion.createDefaultParams(GeneratorType.COLORED_CIRCLES),
                Quality.jpg(100), "path");
        from.setId(1);

        pendingPreset.prepareCandidateFrom(from);
        assertNull(pendingPreset.getPreset());
        assertNotNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        pendingPreset.getCandidate().setName("changed");
        assertFalse(pendingPreset.isCandidateNew());
        assertTrue(pendingPreset.isCandidateChanged());
        pendingPreset.getCandidate().setName("razdwatri");
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        pendingPreset.applyCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertTrue(pendingPreset.getPreset().equals(pendingPreset.getCandidate()));
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        pendingPreset.clearCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        pendingPreset.clearPreset();
        assertNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());
    }
}
