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
        pendingPreset = new PendingPreset("name", "path", new SoutLogger("PendingPresetTest"));
    }

    @Test
    public void testEmpty() {
        assertNull(pendingPreset.get());
        assertNull(pendingPreset.getCandidate());
        try {
            pendingPreset.candidateSaved();
            fail();
        } catch (IllegalStateException e) {
        }
        try {
            pendingPreset.isCandidateNewOrChanged();
            fail();
        } catch (IllegalStateException e) {
        }
        try {
            pendingPreset.applyCandidate();
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testNewDefaultCandidate() {
        pendingPreset.newDefaultCandidate();
        assertNull(pendingPreset.get());
        assertNotNull(pendingPreset.getCandidate());
        assertTrue(pendingPreset.isCandidateNewOrChanged());

        pendingPreset.getCandidate().setId(10);
        pendingPreset.candidateSaved();
        assertNull(pendingPreset.get());
        assertNotNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNewOrChanged());

        pendingPreset.getCandidate().setName("changed");
        assertTrue(pendingPreset.isCandidateNewOrChanged());

        pendingPreset.applyCandidate();
        assertTrue(pendingPreset.get().equals(pendingPreset.getCandidate()));
        assertTrue(pendingPreset.isCandidateNewOrChanged());

        pendingPreset.killCandidate();
        assertNotNull(pendingPreset.get());
        assertNull(pendingPreset.getCandidate());

        try {
            pendingPreset.candidateSaved();
            fail();
        } catch (IllegalStateException e) {
        }
        try {
            pendingPreset.isCandidateNewOrChanged();
            fail();
        } catch (IllegalStateException e) {
        }
        try {
            pendingPreset.applyCandidate();
            fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testPrepareCandidateFrom() {
        Preset from = new Preset("razdwatri",
                GeneratorParams.createDefaultParams(GeneratorType.COLORED_CIRCLES),
                Quality.jpg(100), "path");

        pendingPreset.prepareCandidateFrom(from);
        assertNull(pendingPreset.get());
        assertNotNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNewOrChanged());

        pendingPreset.getCandidate().setId(10);
        pendingPreset.candidateSaved();
        assertNull(pendingPreset.get());
        assertNotNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNewOrChanged());

        pendingPreset.getCandidate().setName("changed");
        assertTrue(pendingPreset.isCandidateNewOrChanged());

        pendingPreset.applyCandidate();
        assertTrue(pendingPreset.get().equals(pendingPreset.getCandidate()));
        assertTrue(pendingPreset.isCandidateNewOrChanged());

        pendingPreset.prepareCandidateFrom(from);
        assertFalse(pendingPreset.get().equals(pendingPreset.getCandidate()));
        assertFalse(pendingPreset.isCandidateNewOrChanged());

        pendingPreset.killCandidate();
        assertNotNull(pendingPreset.get());
        assertNull(pendingPreset.getCandidate());

        try {
            pendingPreset.candidateSaved();
            fail();
        } catch (IllegalStateException e) {
        }
        try {
            pendingPreset.isCandidateNewOrChanged();
            fail();
        } catch (IllegalStateException e) {
        }
        try {
            pendingPreset.applyCandidate();
            fail();
        } catch (IllegalStateException e) {
        }
    }
}
