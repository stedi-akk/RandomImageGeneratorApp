package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(AndroidJUnit4.class)
public class ChooseGeneratorPresenterImplTest {
    private ChooseGeneratorPresenterImpl presenter;
    private PendingPreset pendingPreset;
    private Logger logger;
    private ChooseGeneratorPresenterImpl.UIImpl ui;

    @Before
    public void before() {
        logger = new SoutLogger("ChooseGeneratorPresenterImplTest");
        pendingPreset = spy(new PendingPreset("unsaved", TestUtils.getTestFolder().getAbsolutePath(), logger));
        pendingPreset.prepareCandidateFrom(TestUtils.newSimplePreset());
        presenter = new ChooseGeneratorPresenterImpl(pendingPreset, logger);
        ui = mock(ChooseGeneratorPresenterImpl.UIImpl.class);
    }

    @Test
    public void testGetGeneratorTypes1() {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.Companion.createDefaultParams(GeneratorType.FLAT_COLOR));
        presenter.onAttach(ui);
        presenter.getGeneratorTypes();
        verify(ui, times(1)).showTypes(GeneratorType.Companion.getNON_EFFECT_TYPES(), GeneratorType.FLAT_COLOR);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testGetGeneratorTypes2() {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.Companion.createDefaultEffectParams(GeneratorType.MIRRORED, GeneratorParams.Companion.createDefaultParams(GeneratorType.COLORED_CIRCLES)));
        presenter.onAttach(ui);
        presenter.getGeneratorTypes();
        verify(ui, times(1)).showTypes(GeneratorType.Companion.getNON_EFFECT_TYPES(), GeneratorType.COLORED_CIRCLES);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testChooseGeneratorType1() {
        presenter.onAttach(ui);
        presenter.chooseGeneratorType(GeneratorType.COLORED_RECTANGLE);
        assertTrue(pendingPreset.getCandidate().getGeneratorParams().getType() == GeneratorType.COLORED_RECTANGLE);
        verifyZeroInteractions(ui);
    }

    @Test
    public void testChooseGeneratorType2() {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.Companion.createDefaultEffectParams(GeneratorType.MIRRORED, GeneratorParams.Companion.createDefaultParams(GeneratorType.COLORED_CIRCLES)));
        presenter.onAttach(ui);
        presenter.chooseGeneratorType(GeneratorType.COLORED_RECTANGLE);
        assertTrue(pendingPreset.getCandidate().getGeneratorParams().getType() == GeneratorType.MIRRORED);
        assertTrue(((EffectGeneratorParams) pendingPreset.getCandidate().getGeneratorParams()).getTarget().getType() == GeneratorType.COLORED_RECTANGLE);
        verifyZeroInteractions(ui);
    }

    @Test
    public void testEditChoseGeneratorParams1() {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.Companion.createDefaultParams(GeneratorType.FLAT_COLOR));
        presenter.onAttach(ui);
        presenter.editChoseGeneratorParams();
        verify(ui, times(1)).showEditGeneratorParams(GeneratorType.FLAT_COLOR);
    }

    @Test
    public void testEditChoseGeneratorParams2() {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.Companion.createDefaultEffectParams(GeneratorType.MIRRORED, GeneratorParams.Companion.createDefaultParams(GeneratorType.COLORED_CIRCLES)));
        presenter.onAttach(ui);
        presenter.editChoseGeneratorParams();
        verify(ui, times(1)).showEditGeneratorParams(GeneratorType.COLORED_CIRCLES);
    }
}
