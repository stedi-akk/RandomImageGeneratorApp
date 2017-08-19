package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class GenerationStepsPresenterImplTest {
    private GenerationStepsPresenterImpl presenter;
    private PendingPreset pendingPreset;
    private Logger logger;
    private GenerationStepsPresenterImpl.UIImpl ui;

    @Before
    public void before() {
        logger = new SoutLogger("GenerationStepsPresenterImplTest");
        pendingPreset = spy(new PendingPreset("unsaved", TestUtils.getTestFolder().getAbsolutePath(), logger));
        presenter = new GenerationStepsPresenterImpl(pendingPreset, logger);
        ui = mock(GenerationStepsPresenterImpl.UIImpl.class);
    }

    @Test
    public void testIsNewTrue() {
        presenter.onAttach(ui);
        presenter.setIsNew(true);
        verify(pendingPreset, times(1)).newDefaultCandidate();
        verify(ui, times(1)).showFirstStep();
        verifyNoMoreInteractions(pendingPreset);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testIsNewFalse() {
        Preset preset = new Preset("name", GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR), Quality.jpg(100), TestUtils.getTestFolder().getAbsolutePath());
        preset.setTimestamp(System.currentTimeMillis());
        preset.setId(66);
        pendingPreset.prepareCandidateFrom(preset);
        presenter.onAttach(ui);
        presenter.setIsNew(false);
        verify(ui, times(1)).showFinishStep();
        verifyNoMoreInteractions(ui);
    }

    @Test(expected = IllegalStateException.class)
    public void testIsNewFalseFailed() {
        presenter.onAttach(ui);
        presenter.setIsNew(false);
    }

    @Test
    public void testRelease() {
        presenter.onAttach(ui);
        presenter.setIsNew(true);
        verify(pendingPreset, times(1)).newDefaultCandidate();
        verify(ui, times(1)).showFirstStep();
        presenter.release();
        assertNull(pendingPreset.getCandidate());
        verifyNoMoreInteractions(ui);
    }
}
