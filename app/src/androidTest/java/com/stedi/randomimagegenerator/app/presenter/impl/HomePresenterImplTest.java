package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.app.FakePresetRepository;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.LockedBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.List;

import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class HomePresenterImplTest {
    private static final int FAKE_PRESEST_REPO_SIZE = 10;

    private HomePresenterImpl presenter;
    private PresetRepository repository;
    private PendingPreset pendingPreset;
    private HomePresenterImpl.UIImpl ui;
    private ArgumentCaptor<Preset> pendingPresetCaptor;
    private ArgumentCaptor<List<Preset>> presetsCaptor;

    @Before
    public void before() {
        repository = spy(new FakePresetRepository(FAKE_PRESEST_REPO_SIZE));
        pendingPreset = new PendingPreset();
        presenter = new HomePresenterImpl(repository, pendingPreset, Schedulers.immediate(), Schedulers.immediate(), new LockedBus(ThreadEnforcer.ANY));
        ui = mock(HomePresenterImpl.UIImpl.class);
        pendingPresetCaptor = ArgumentCaptor.forClass(Preset.class);
        //noinspection unchecked
        presetsCaptor = ArgumentCaptor.forClass(List.class);
    }

    @Test
    public void testFetchPresets() {
        // create pending
        pendingPreset.newDefaultCandidate();
        pendingPreset.applyCandidate();
        pendingPreset.clearCandidate();

        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(pendingPresetCaptor.capture(), presetsCaptor.capture());

        assertTrue(pendingPreset.getPreset() == pendingPresetCaptor.getValue());
        List<Preset> presets = presetsCaptor.getValue();
        assertTrue(presets.size() == FAKE_PRESEST_REPO_SIZE);

        // check if sorted correctly
        Preset prevPreset = null;
        for (Preset preset : presets) {
            if (prevPreset != null && preset.getTimestamp() > prevPreset.getTimestamp()) {
                fail();
            }
            prevPreset = preset;
        }

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testFetchPresetsFailed() throws Exception {
        // create pending
        pendingPreset.newDefaultCandidate();
        pendingPreset.applyCandidate();
        pendingPreset.clearCandidate();

        when(repository.getAll()).thenThrow(new NullPointerException("nope"));

        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onFailedToFetchPresets();

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testEditPreset() {
        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());

        Preset preset = presetsCaptor.getValue().get(0);
        presenter.editPreset(preset);
        assertNotNull(pendingPreset.getCandidate());
        assertTrue(pendingPreset.getCandidate().equals(preset));
        assertFalse(pendingPreset.isCandidateNew());

        verify(ui, times(1)).showEditPreset();

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testNewPreset() {
        presenter.onAttach(ui);
        presenter.newPreset();

        assertNotNull(pendingPreset.getCandidate());
        assertTrue(pendingPreset.isCandidateNew());

        verify(ui, times(1)).showCreatePreset();

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testDeletePendingPreset() {
        // create pending
        pendingPreset.newDefaultCandidate();
        pendingPreset.applyCandidate();
        pendingPreset.clearCandidate();

        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(pendingPresetCaptor.capture(), any());

        Preset preset = pendingPresetCaptor.getValue();
        presenter.deletePreset(preset);
        assertNull(pendingPreset.getPreset());

        verify(ui, times(1)).onPresetDeleted(preset);

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testDeletePresetConfirm() throws Exception {
        presenter.onAttach(ui);
        presenter.fetchPresets();

        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());
        Preset preset = presetsCaptor.getValue().get(0);
        presenter.deletePreset(preset);

        verify(ui, times(1)).showConfirmDeletePreset(preset);
        presenter.confirmDeletePreset(true);

        verify(ui, times(1)).onPresetDeleted(preset);
        assertTrue(repository.getAll().size() == FAKE_PRESEST_REPO_SIZE - 1);

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testDeletePresetConfirmFailed() throws Exception {
        doThrow(new NullPointerException("nope")).when(repository).remove(anyInt());

        presenter.onAttach(ui);
        presenter.fetchPresets();

        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());
        Preset preset = presetsCaptor.getValue().get(0);
        presenter.deletePreset(preset);

        verify(ui, times(1)).showConfirmDeletePreset(preset);
        presenter.confirmDeletePreset(true);

        verify(ui, times(1)).onFailedToDeletePreset();
        assertTrue(repository.getAll().size() == FAKE_PRESEST_REPO_SIZE);

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testDeletePresetCancel() throws Exception {
        presenter.onAttach(ui);
        presenter.fetchPresets();

        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());
        Preset preset = presetsCaptor.getValue().get(0);
        presenter.deletePreset(preset);

        verify(ui, times(1)).showConfirmDeletePreset(preset);
        presenter.confirmDeletePreset(false);

        assertTrue(repository.getAll().size() == FAKE_PRESEST_REPO_SIZE);

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testStartGenerationConfirm() {
        presenter.onAttach(ui);
        presenter.fetchPresets();

        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());
        Preset preset = presetsCaptor.getValue().get(0);
        presenter.startGeneration(preset);

        verify(ui, times(1)).showConfirmGeneratePreset(preset);
        presenter.confirmStartGeneration(true);

        verify(ui, times(1)).showGenerationDialog(preset);

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testStartGenerationCancel() {
        presenter.onAttach(ui);
        presenter.fetchPresets();

        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());
        Preset preset = presetsCaptor.getValue().get(0);
        presenter.startGeneration(preset);

        verify(ui, times(1)).showConfirmGeneratePreset(preset);
        presenter.confirmStartGeneration(false);

        verifyNoMoreInteractions(ui);
    }
}
