package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.ColoredCirclesParams;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.SimpleIntegerParams;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class SimpleIntegerParamsPresenterImplTest {
    private SimpleIntegerParamsPresenterImpl presenter;
    private SimpleIntegerParams integerParams;
    private PendingPreset pendingPreset;
    private Logger logger;

    private SimpleIntegerParamsPresenterImpl.UIImpl ui;

    @Before
    public void before() {
        logger = new SoutLogger("SimpleIntegerParamsPresenterImplTest");
        pendingPreset = new PendingPreset(logger);
        integerParams = new ColoredCirclesParams();
        pendingPreset.prepareCandidateFrom(TestUtils.newSimplePreset());
        pendingPreset.getCandidate().setGeneratorParams(integerParams);
        presenter = new SimpleIntegerParamsPresenterImpl(pendingPreset, logger);
        ui = mock(SimpleIntegerParamsPresenterImpl.UIImpl.class);
    }

    @Test
    public void testGetValuesRandom() {
        presenter.onAttach(ui);
        presenter.getValues();
        verify(ui, times(1)).showRandomValue();
        presenter.setValue(10);
        presenter.getValues();
        verify(ui, times(1)).showValue(10);
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testGetValuesNonRandom() {
        integerParams.setValue(10);
        presenter.onAttach(ui);
        presenter.getValues();
        verify(ui, times(1)).showValue(10);
        presenter.setRandomValue();
        presenter.getValues();
        verify(ui, times(1)).showRandomValue();
        verifyNoMoreInteractions(ui);
    }
}
