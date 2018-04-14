package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.repository.FakePresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SuppressWarnings("MissingPermission")
@RunWith(AndroidJUnit4.class)
public class ApplyGenerationPresenterImplTest {
    private ApplyGenerationPresenterImpl presenter;
    private FakePresetRepository presetRepository;
    private PendingPreset pendingPreset;

    private ApplyGenerationPresenterImpl.UIImpl ui;

    @Before
    public void before() {
        Rig.enableDebugLogging(true);
        Logger logger = new SoutLogger("ApplyGenerationPresenterImplTest");
        pendingPreset = new PendingPreset("unsaved", TestUtils.getTestFolder().getAbsolutePath(), logger);
        pendingPreset.prepareCandidateFrom(TestUtils.newSimplePreset());
        presetRepository = spy(new FakePresetRepository(0));
        presenter = new ApplyGenerationPresenterImpl(pendingPreset, presetRepository, TestUtils.getTestFolder().getAbsolutePath(),
                Schedulers.immediate(), Schedulers.immediate(), new CachedBus(ThreadEnforcer.ANY, logger), logger);
        ui = mock(ApplyGenerationPresenterImpl.UIImpl.class);
    }

    @AfterClass
    public static void afterClass() {
        TestUtils.deleteTestFolder();
    }

    @Test
    public void testSavePresetNormal() throws Exception {
        presenter.onAttach(ui);

        presenter.savePreset("testSavePreset");
        verify(ui, times(1)).onPresetSaved();
        assertTrue(pendingPreset.getCandidate().getName().equals("testSavePreset"));
        assertTrue(pendingPreset.getCandidate().getId() > 0);
        List<Preset> repoPresets = presetRepository.getAll();
        assertTrue(repoPresets.size() == 1);
        assertTrue(repoPresets.get(0).equals(pendingPreset.getCandidate()));

        doThrow(new NullPointerException()).when(presetRepository).save(any());
        presenter.savePreset("failed");
        verify(ui, times(1)).failedToSavePreset();
        assertTrue(pendingPreset.getCandidate().getName().equals("testSavePreset"));
        repoPresets = presetRepository.getAll();
        assertTrue(repoPresets.size() == 1);
        assertTrue(repoPresets.get(0).equals(pendingPreset.getCandidate()));

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testSavePresetPending() throws Exception {
        pendingPreset.getCandidate().setName("123");
        pendingPreset.applyCandidate();
        presenter.onAttach(ui);

        doThrow(new NullPointerException()).when(presetRepository).save(any());
        presenter.savePreset("failed");
        verify(ui, times(1)).failedToSavePreset();
        assertTrue(pendingPreset.get().getId() == 0);
        assertEquals(pendingPreset.get(), pendingPreset.getCandidate());

        doCallRealMethod().when(presetRepository).save(any());
        presenter.savePreset("testSavePreset");
        verify(ui, times(1)).onPresetSaved();
        assertNull(pendingPreset.get());
        assertTrue(pendingPreset.getCandidate().getName().equals("testSavePreset"));
        assertTrue(pendingPreset.getCandidate().getId() > 0);
        List<Preset> repoPresets = presetRepository.getAll();
        assertTrue(repoPresets.size() == 1);
        assertTrue(repoPresets.get(0).equals(pendingPreset.getCandidate()));

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testStartGeneration() {
        pendingPreset.newDefaultCandidate();
        presenter.onAttach(ui);
        assertNull(pendingPreset.get());
        presenter.startGeneration(pendingPreset.getCandidate());
        assertTrue(pendingPreset.get().equals(pendingPreset.getCandidate()));
    }
}
