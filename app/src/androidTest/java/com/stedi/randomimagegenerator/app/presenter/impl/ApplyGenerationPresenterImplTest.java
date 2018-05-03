package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.app.FakePresetRepository;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.LockedBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.List;

import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(AndroidJUnit4.class)
public class ApplyGenerationPresenterImplTest {
    private static final String DEFAULT_NAME = "unsaved";

    private ApplyGenerationPresenterImpl presenter;
    private FakePresetRepository presetRepository;
    private PendingPreset pendingPreset;

    private ApplyGenerationPresenterImpl.UIImpl ui;

    @Before
    public void before() {
        pendingPreset = new PendingPreset();
        presetRepository = spy(new FakePresetRepository(0));
        presenter = new ApplyGenerationPresenterImpl(pendingPreset, presetRepository, TestUtils.getTestFolder().getAbsolutePath(), DEFAULT_NAME,
                Schedulers.immediate(), Schedulers.immediate(), new LockedBus(ThreadEnforcer.ANY));
        ui = mock(ApplyGenerationPresenterImpl.UIImpl.class);
    }

    @Test
    public void testGetters() {
        presenter.onAttach(ui);

        // check new preset
        pendingPreset.newDefaultCandidate();
        assertTrue(presenter.isPresetNew());
        assertFalse(presenter.isPresetChanged());
        Preset preset = presenter.getPreset();
        assertTrue(preset.getName().equals(DEFAULT_NAME));
        assertTrue(preset.getPathToSave().equals(new File(TestUtils.getTestFolder().getAbsolutePath(), "0").getPath()));

        // check updated preset
        pendingPreset.getCandidate().setName("changed");
        assertTrue(presenter.isPresetNew());
        assertFalse(presenter.isPresetChanged());
        preset = presenter.getPreset();
        assertTrue(preset.getName().equals(DEFAULT_NAME));
        assertTrue(preset.getPathToSave().equals(new File(TestUtils.getTestFolder().getAbsolutePath(), "0").getPath()));

        pendingPreset.clearCandidate();

        // check preset from existing
        preset = TestUtils.newSimplePreset();
        preset.setName("name");
        preset.setPathToSave("path");
        pendingPreset.prepareCandidateFrom(preset);
        assertFalse(presenter.isPresetNew());
        assertFalse(presenter.isPresetChanged());
        preset = presenter.getPreset();
        assertTrue(preset.getName().equals("name"));
        assertTrue(preset.getPathToSave().equals("path"));

        // check updated preset
        pendingPreset.getCandidate().setName("changed");
        assertFalse(presenter.isPresetNew());
        assertTrue(presenter.isPresetChanged());
        preset = presenter.getPreset();
        assertTrue(preset.getName().equals("changed"));
        assertTrue(preset.getPathToSave().equals("path"));
    }

    @Test
    public void testStartGeneration() {
        presenter.onAttach(ui);

        // new preset
        pendingPreset.newDefaultCandidate();
        presenter.startGeneration();
        assertTrue(pendingPreset.getCandidate().getName().equals(DEFAULT_NAME));
        assertTrue(pendingPreset.getCandidate().getPathToSave().equals(new File(TestUtils.getTestFolder().getAbsolutePath(), "0").getPath()));
        assertNotNull(pendingPreset.getPreset());
        assertTrue(pendingPreset.getPreset().equals(pendingPreset.getCandidate()));
        verify(ui, times(1)).showGenerationDialog(pendingPreset.getCandidate());

        pendingPreset.clearPreset();
        pendingPreset.clearCandidate();

        // preset from existing
        Preset preset = TestUtils.newSimplePreset();
        preset.setName("name");
        preset.setPathToSave("path");
        pendingPreset.prepareCandidateFrom(preset);
        presenter.startGeneration();
        assertTrue(pendingPreset.getCandidate().getName().equals("name"));
        assertTrue(pendingPreset.getCandidate().getPathToSave().equals("path"));
        assertNull(pendingPreset.getPreset());
        verify(ui, times(1)).showGenerationDialog(pendingPreset.getCandidate());

        // update preset
        preset.setName("changed");
        presenter.startGeneration();
        assertTrue(pendingPreset.getCandidate().getName().equals(DEFAULT_NAME));
        assertTrue(pendingPreset.getCandidate().getPathToSave().equals(new File(TestUtils.getTestFolder().getAbsolutePath(), "0").getPath()));
        assertNotNull(pendingPreset.getPreset());
        assertTrue(pendingPreset.getPreset().equals(pendingPreset.getCandidate()));
        verify(ui, times(2)).showGenerationDialog(pendingPreset.getCandidate());

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testSavePresetExisting() {
        presenter.onAttach(ui);

        Preset preset = TestUtils.newSimplePreset();
        preset.setName("name");
        preset.setPathToSave("path");
        pendingPreset.prepareCandidateFrom(preset);
        presenter.savePreset("saved");
        verify(ui, times(1)).onPresetSaved();
        assertTrue(pendingPreset.getCandidate().getName().equals("saved"));
        assertTrue(pendingPreset.getCandidate().getId() > 0);
        assertTrue(pendingPreset.getCandidate().getPathToSave()
                .equals(new File(TestUtils.getTestFolder().getAbsolutePath(), String.valueOf(pendingPreset.getCandidate().getId())).getPath()));
        List<Preset> repoPresets = presetRepository.getAll();
        assertTrue(repoPresets.size() == 1);
        assertTrue(repoPresets.get(0).equals(pendingPreset.getCandidate()));

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testSavePresetExistingFail() {
        presenter.onAttach(ui);

        doThrow(new NullPointerException()).when(presetRepository).save(any());

        Preset preset = TestUtils.newSimplePreset();
        preset.setName("name");
        preset.setPathToSave("path");
        pendingPreset.prepareCandidateFrom(preset);
        presenter.savePreset("failed");
        verify(ui, times(1)).failedToSavePreset();
        assertTrue(pendingPreset.getCandidate().getName().equals("name"));
        assertTrue(pendingPreset.getCandidate().getPathToSave().equals("path"));
        assertTrue(presetRepository.getAll().isEmpty());

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testSavePresetPending() {
        presenter.onAttach(ui);

        pendingPreset.newDefaultCandidate();
        pendingPreset.applyCandidate();
        pendingPreset.clearCandidate();

        pendingPreset.prepareCandidateFrom(pendingPreset.getPreset());
        presenter.savePreset("saved");
        verify(ui, times(1)).onPresetSaved();
        assertTrue(pendingPreset.getCandidate().getName().equals("saved"));
        assertTrue(pendingPreset.getCandidate().getId() > 0);
        assertTrue(pendingPreset.getCandidate().getPathToSave()
                .equals(new File(TestUtils.getTestFolder().getAbsolutePath(), String.valueOf(pendingPreset.getCandidate().getId())).getPath()));
        List<Preset> repoPresets = presetRepository.getAll();
        assertTrue(repoPresets.size() == 1);
        assertTrue(repoPresets.get(0).equals(pendingPreset.getCandidate()));

        assertNull(pendingPreset.getPreset());

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testSavePresetPendingFail() {
        presenter.onAttach(ui);

        doThrow(new NullPointerException()).when(presetRepository).save(any());

        pendingPreset.newDefaultCandidate();
        pendingPreset.getCandidate().setName("name");
        pendingPreset.getCandidate().setPathToSave("path");
        pendingPreset.applyCandidate();
        pendingPreset.clearCandidate();

        pendingPreset.prepareCandidateFrom(pendingPreset.getPreset());
        presenter.savePreset("failed");
        verify(ui, times(1)).failedToSavePreset();
        assertTrue(pendingPreset.getCandidate().getName().equals("name"));
        assertTrue(pendingPreset.getCandidate().getPathToSave().equals("path"));
        assertTrue(presetRepository.getAll().isEmpty());

        assertNotNull(pendingPreset.getPreset());

        verifyNoMoreInteractions(ui);
    }
}
