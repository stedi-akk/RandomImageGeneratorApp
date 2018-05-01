package com.stedi.randomimagegenerator.app.model.data;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.TestUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PendingPresetTest {
    private PendingPreset pendingPreset;

    @Test
    public void testEmpty() {
        pendingPreset = new PendingPreset();

        assertNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateChanged());
        assertFalse(pendingPreset.isCandidateNew());

        pendingPreset.applyCandidate();
        pendingPreset.clearCandidate();
        pendingPreset.clearPreset();
    }

    @Test
    public void testNewDefaultCandidate() {
        pendingPreset = new PendingPreset();

        // new
        pendingPreset.newDefaultCandidate();
        assertNull(pendingPreset.getPreset());
        assertNotNull(pendingPreset.getCandidate());
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // change candidate
        pendingPreset.getCandidate().setName("name1");
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // apply candidate
        pendingPreset.applyCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertTrue(pendingPreset.getPreset().equals(pendingPreset.getCandidate()));
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // clear candidate
        pendingPreset.clearCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // new
        pendingPreset.newDefaultCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertNotNull(pendingPreset.getCandidate());
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // change candidate
        pendingPreset.getCandidate().setName("name2");
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // apply candidate
        pendingPreset.applyCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertTrue(pendingPreset.getPreset().equals(pendingPreset.getCandidate()));
        assertTrue(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // clear candidate
        pendingPreset.clearCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // clear preset
        pendingPreset.clearPreset();
        assertNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());
    }

    @Test
    public void testPrepareCandidateFrom() {
        pendingPreset = new PendingPreset();

        Preset from = TestUtils.newSimplePreset();
        from.setName("name1");
        from.setId(1);

        // new
        pendingPreset.prepareCandidateFrom(from);
        assertNull(pendingPreset.getPreset());
        assertNotNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // change candidate
        pendingPreset.getCandidate().setName("");
        assertFalse(pendingPreset.isCandidateNew());
        assertTrue(pendingPreset.isCandidateChanged());

        // apply candidate
        pendingPreset.applyCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertTrue(pendingPreset.getPreset().equals(pendingPreset.getCandidate()));
        assertFalse(pendingPreset.isCandidateNew());
        assertTrue(pendingPreset.isCandidateChanged());

        // clear candidate
        pendingPreset.clearCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        from = TestUtils.newSimplePreset();
        from.setName("name2");
        from.setId(1);

        // new
        pendingPreset.prepareCandidateFrom(from);
        assertNotNull(pendingPreset.getPreset());
        assertNotNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // change candidate
        pendingPreset.getCandidate().setName("");
        assertFalse(pendingPreset.isCandidateNew());
        assertTrue(pendingPreset.isCandidateChanged());

        // apply candidate
        pendingPreset.applyCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertTrue(pendingPreset.getPreset().equals(pendingPreset.getCandidate()));
        assertFalse(pendingPreset.isCandidateNew());
        assertTrue(pendingPreset.isCandidateChanged());

        // clear candidate
        pendingPreset.clearCandidate();
        assertNotNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());

        // clear preset
        pendingPreset.clearPreset();
        assertNull(pendingPreset.getPreset());
        assertNull(pendingPreset.getCandidate());
        assertFalse(pendingPreset.isCandidateNew());
        assertFalse(pendingPreset.isCandidateChanged());
    }
}
