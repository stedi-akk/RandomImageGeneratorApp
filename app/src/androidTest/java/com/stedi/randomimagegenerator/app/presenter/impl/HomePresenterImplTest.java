package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.model.repository.PresetRepository;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.misusing.NullInsteadOfMockException;

import java.util.ArrayList;
import java.util.List;

import rx.schedulers.Schedulers;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class HomePresenterImplTest {
    private HomePresenterImpl presenter;
    private PresetRepository repository;
    private List<Preset> repoPresets;
    private PendingPreset pendingPreset;
    private HomePresenterImpl.UIImpl ui;

    @Before
    public void before() throws Exception {
        repository = mock(PresetRepository.class);
        repoPresets = new ArrayList<>();
        repoPresets.add(createTestPreset(1, GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR)));
        repoPresets.add(createTestPreset(2, GeneratorParams.createDefaultParams(GeneratorType.COLORED_NOISE)));
        repoPresets.add(createTestPreset(3, GeneratorParams.createDefaultParams(GeneratorType.COLORED_RECTANGLE)));
        when(repository.getAll()).thenReturn(repoPresets);
        SoutLogger logger = new SoutLogger("HomePresenterImplTest");
        pendingPreset = new PendingPreset("unsaved", "path", logger);
        pendingPreset.newDefaultCandidate();
        pendingPreset.applyCandidate();
        pendingPreset.killCandidate();
        presenter = new HomePresenterImpl(repository, pendingPreset, Schedulers.immediate(), Schedulers.immediate(), Schedulers.immediate(),
                new CachedBus(ThreadEnforcer.ANY), logger);
        ui = mock(HomePresenterImpl.UIImpl.class);
    }

    @Test
    public void testFetchPresets() throws Exception {
        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onPresetsFetched(pendingPreset.get(), repoPresets);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testFetchPresetsFailed() throws Exception {
        when(repository.getAll()).thenThrow(new NullPointerException("nope"));
        presenter.onAttach(ui);
        presenter.fetchPresets();
        verify(ui, times(1)).onFailedToFetchPresets();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testEditPreset() {
        presenter.onAttach(ui);
        presenter.editPreset(repoPresets.get(0));
        verify(ui, times(1)).showEditPreset();
        verifyNoMoreInteractions(ui);
    }

    private Preset createTestPreset(int id, GeneratorParams generatorParams) {
        Preset preset = new Preset("name" + id, generatorParams, Quality.png(), "path");
        preset.setTimestamp(System.currentTimeMillis());
        preset.setId(id);
        return preset;
    }
}
