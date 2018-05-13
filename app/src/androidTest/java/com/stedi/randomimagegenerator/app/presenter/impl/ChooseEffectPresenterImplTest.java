package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(AndroidJUnit4.class)
public class ChooseEffectPresenterImplTest {
    private ChooseEffectPresenterImpl presenter;
    private PendingPreset pendingPreset;

    private ChooseEffectPresenterImpl.UIImpl ui;

    @Before
    public void before() {
        pendingPreset = new PendingPreset();
        pendingPreset.prepareCandidateFrom(TestUtils.newSimplePreset());
        presenter = new ChooseEffectPresenterImpl(pendingPreset);
        ui = mock(ChooseEffectPresenterImpl.UIImpl.class);
    }

    @Test
    public void testGetEffectTypesNull() {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.Companion.createDefaultParams(GeneratorType.COLORED_RECTANGLE));

        presenter.onAttach(ui);
        presenter.getEffectTypes();

        verify(ui, times(1)).showTypes(GeneratorType.Companion.getEFFECT_TYPES(), null, GeneratorType.COLORED_RECTANGLE);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testGetEffectTypesNonNull() {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.Companion.createDefaultEffectParams(GeneratorType.MIRRORED, GeneratorParams.Companion.createDefaultParams(GeneratorType.COLORED_CIRCLES)));

        presenter.onAttach(ui);
        presenter.getEffectTypes();

        verify(ui, times(1)).showTypes(GeneratorType.Companion.getEFFECT_TYPES(), GeneratorType.MIRRORED, GeneratorType.COLORED_CIRCLES);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testChooseEffectType1() {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.Companion.createDefaultParams(GeneratorType.COLORED_RECTANGLE));

        presenter.onAttach(ui);
        presenter.chooseEffectType(GeneratorType.TEXT_OVERLAY);

        assertTrue(pendingPreset.getCandidate().getGeneratorParams().getType() == GeneratorType.TEXT_OVERLAY);
        assertTrue(((EffectGeneratorParams) pendingPreset.getCandidate().getGeneratorParams()).getTarget().getType() == GeneratorType.COLORED_RECTANGLE);
        verifyZeroInteractions(ui);
    }

    @Test
    public void testChooseEffectType2() {
        pendingPreset.getCandidate().setGeneratorParams(GeneratorParams.Companion.createDefaultEffectParams(GeneratorType.MIRRORED, GeneratorParams.Companion.createDefaultParams(GeneratorType.COLORED_CIRCLES)));

        presenter.onAttach(ui);
        presenter.chooseEffectType(GeneratorType.TEXT_OVERLAY);

        assertTrue(pendingPreset.getCandidate().getGeneratorParams().getType() == GeneratorType.TEXT_OVERLAY);
        assertTrue(((EffectGeneratorParams) pendingPreset.getCandidate().getGeneratorParams()).getTarget().getType() == GeneratorType.COLORED_CIRCLES);
        verifyZeroInteractions(ui);
    }
}
