package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.repository.FakePresetRepository;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.HomePresenter;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.List;

import rx.schedulers.Schedulers;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("MissingPermission")
@RunWith(AndroidJUnit4.class)
public class HomePresenterImplTest {
    private static final int PRESETS_REPO_INITIAL_COUNT = 3;

    private HomePresenterImpl presenter;
    private PresetRepository repository;
    private PendingPreset pendingPreset;
    private HomePresenterImpl.UIImpl ui;
    private ArgumentCaptor<Preset> pendingPresetCaptor;
    private ArgumentCaptor<List<Preset>> presetsCaptor;

    @Before
    public void before() throws Exception {
        Rig.enableDebugLogging(true);
        repository = spy(new FakePresetRepository(PRESETS_REPO_INITIAL_COUNT));
        SoutLogger logger = new SoutLogger("HomePresenterImplTest");
        pendingPreset = new PendingPreset("unsaved", TestUtils.getTestFolder().getAbsolutePath(), logger);
        presenter = new HomePresenterImpl(repository, pendingPreset, Schedulers.immediate(), Schedulers.immediate(), Schedulers.immediate(),
                new CachedBus(ThreadEnforcer.ANY), logger);
        ui = mock(HomePresenterImpl.UIImpl.class);
        pendingPresetCaptor = ArgumentCaptor.forClass(Preset.class);
        presetsCaptor = ArgumentCaptor.forClass((Class) List.class);
    }

    @AfterClass
    public static void afterClass() {
        TestUtils.deleteTestFolder();
    }

    @Test
    public void testFetchPresets() throws Exception {
        pendingPreset.newDefaultCandidate();
        pendingPreset.applyCandidate();
        pendingPreset.killCandidate();
        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(pendingPresetCaptor.capture(), presetsCaptor.capture());
        assertTrue(pendingPreset.get() == pendingPresetCaptor.getValue());
        assertTrue(presetsCaptor.getValue().size() == 3);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testFetchPresetsFailed() throws Exception {
        pendingPreset.newDefaultCandidate();
        pendingPreset.applyCandidate();
        pendingPreset.killCandidate();
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
        presenter.editPreset(presetsCaptor.getValue().get(0));
        verify(ui, times(1)).showEditPreset();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testDeletePendingPreset() {
        pendingPreset.newDefaultCandidate();
        pendingPreset.applyCandidate();
        pendingPreset.killCandidate();
        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(pendingPresetCaptor.capture(), any());
        Preset preset = pendingPresetCaptor.getValue();
        presenter.deletePreset(preset);
        assertNull(pendingPreset.get());
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
        verify(ui, times(1)).showConfirmLastAction(HomePresenter.Confirm.DELETE_PRESET);
        presenter.confirmLastAction();
        verify(ui, times(1)).onPresetDeleted(preset);
        assertTrue(repository.getAll().size() == PRESETS_REPO_INITIAL_COUNT - 1);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testDeletePresetConfirmFailed() throws Exception {
        doThrow(new NullPointerException("nope")).when(repository).remove(anyInt());
        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());
        presenter.deletePreset(presetsCaptor.getValue().get(0));
        verify(ui, times(1)).showConfirmLastAction(HomePresenter.Confirm.DELETE_PRESET);
        presenter.confirmLastAction();
        verify(ui, times(1)).onFailedToDeletePreset();
        assertTrue(repository.getAll().size() == PRESETS_REPO_INITIAL_COUNT);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testDeletePresetCancel() throws Exception {
        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());
        Preset preset = presetsCaptor.getValue().get(0);
        presenter.deletePreset(preset);
        verify(ui, times(1)).showConfirmLastAction(HomePresenter.Confirm.DELETE_PRESET);
        presenter.cancelLastAction();
        assertTrue(repository.getAll().size() == PRESETS_REPO_INITIAL_COUNT);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testStartGenerationConfirm() throws Exception {
        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());
        Preset preset = presetsCaptor.getValue().get(0);
        presenter.startGeneration(preset);
        verify(ui, times(1)).showConfirmLastAction(HomePresenter.Confirm.GENERATE_FROM_PRESET);
        presenter.confirmLastAction();
        verify(ui, times(1)).onStartGeneration();
        verify(ui, times(1)).onGenerated(any());
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testStartGenerationCancel() throws Exception {
        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(any(), presetsCaptor.capture());
        Preset preset = presetsCaptor.getValue().get(0);
        presenter.startGeneration(preset);
        verify(ui, times(1)).showConfirmLastAction(HomePresenter.Confirm.GENERATE_FROM_PRESET);
        presenter.cancelLastAction();
        verifyNoMoreInteractions(ui);
    }
}
