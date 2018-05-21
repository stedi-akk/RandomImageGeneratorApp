package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredNoiseParams;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter;
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ColoredNoiseParamsPresenterImplTest {
    private ColoredNoiseParamsPresenterImpl presenter;
    private ColoredNoiseParams noiseParams;

    private ColoredNoiseParamsPresenter.UIImpl ui;

    @Before
    public void before() {
        PendingPreset pendingPreset = new PendingPreset();
        noiseParams = new ColoredNoiseParams();
        pendingPreset.prepareCandidateFrom(TestUtils.newSimplePreset());
        pendingPreset.getCandidate().setGeneratorParams(noiseParams);
        presenter = new ColoredNoiseParamsPresenterImpl(pendingPreset);
        ui = mock(ColoredNoiseParamsPresenter.UIImpl.class);
    }

    @Test
    public void test() {
        presenter.onAttach(ui);
        presenter.getValues();

        verify(ui, times(1)).showOrientation(noiseParams.getNoiseOrientation());
        verify(ui, timeout(1)).showType(noiseParams.getNoiseType());

        presenter.setOrientation(ColoredNoiseGenerator.Orientation.VERTICAL);
        presenter.setType(ColoredNoiseGenerator.Type.TYPE_5);
        assertTrue(noiseParams.getNoiseOrientation() == ColoredNoiseGenerator.Orientation.VERTICAL);
        assertTrue(noiseParams.getNoiseType() == ColoredNoiseGenerator.Type.TYPE_5);

        presenter.getValues();
        verify(ui, times(1)).showOrientation(ColoredNoiseGenerator.Orientation.VERTICAL);
        verify(ui, timeout(1)).showType(ColoredNoiseGenerator.Type.TYPE_5);
    }
}
