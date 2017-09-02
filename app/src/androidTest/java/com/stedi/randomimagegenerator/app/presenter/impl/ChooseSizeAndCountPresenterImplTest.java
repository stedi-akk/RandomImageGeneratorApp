package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseSizeAndCountPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class ChooseSizeAndCountPresenterImplTest {
    private ChooseSizeAndCountPresenterImpl presenter;
    private PendingPreset pendingPreset;
    private Logger logger;

    private ChooseSizeAndCountPresenterImpl.UIImpl ui;

    @Before
    public void before() {
        logger = new SoutLogger("ChooseSizeAndCountPresenterImplTest");
        pendingPreset = new PendingPreset("unsaved", TestUtils.getTestFolder().getAbsolutePath(), logger);
        pendingPreset.prepareCandidateFrom(TestUtils.newSimplePreset());
        Preset preset = pendingPreset.getCandidate();
        preset.setWidth(100);
        preset.setHeight(200);
        preset.setCount(3);
        presenter = new ChooseSizeAndCountPresenterImpl(pendingPreset, logger);
        ui = mock(ChooseSizeAndCountPresenterImpl.UIImpl.class);
    }

    @Test
    public void testGetValues() {
        presenter.onAttach(ui);

        presenter.getValues();
        verify(ui, times(1)).showCount(3);
        verify(ui, times(1)).showHeight(200);
        verify(ui, times(1)).showWidth(100);

        Preset preset = pendingPreset.getCandidate();
        preset.setWidthRange(10, 100, 11);
        presenter.getValues();
        verify(ui, times(1)).showWidthRange(10, 100, 11);
        verify(ui, times(2)).showHeight(200);

        preset.setHeightRange(120, 10, 5);
        presenter.getValues();
        verify(ui, times(2)).showWidthRange(10, 100, 11);
        verify(ui, times(1)).showHeightRange(120, 10, 5);

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testSetCount() {
        presenter.onAttach(ui);
        presenter.setCount(0);
        verify(ui, times(1)).onError(ChooseSizeAndCountPresenter.Error.INCORRECT_COUNT);
        assertTrue(pendingPreset.getCandidate().getCount() == 3);

        presenter.setCount(10);
        assertTrue(pendingPreset.getCandidate().getCount() == 10);

        Preset preset = pendingPreset.getCandidate();
        preset.setWidthRange(1, 11, 1);
        preset.setHeightRange(1, 11, 1);
        presenter.setCount(1);
        verify(ui, times(2)).onError(ChooseSizeAndCountPresenter.Error.INCORRECT_COUNT);

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testSetWidthHeight() {
        presenter.onAttach(ui);
        presenter.setWidth(0);
        verify(ui, times(1)).onError(ChooseSizeAndCountPresenter.Error.INCORRECT_WIDTH);
        assertTrue(pendingPreset.getCandidate().getWidth() == 100);

        presenter.setWidth(66);
        assertTrue(pendingPreset.getCandidate().getWidth() == 66);

        presenter.setHeight(0);
        verify(ui, times(1)).onError(ChooseSizeAndCountPresenter.Error.INCORRECT_HEIGHT);
        assertTrue(pendingPreset.getCandidate().getHeight() == 200);

        presenter.setHeight(77);
        assertTrue(pendingPreset.getCandidate().getHeight() == 77);

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testSetWidthHeightRange() {
        presenter.onAttach(ui);
        presenter.setWidthRange(0, 100, 1);
        verify(ui, times(1)).onError(ChooseSizeAndCountPresenter.Error.INCORRECT_WIDTH_RANGE);
        assertTrue(pendingPreset.getCandidate().getWidthRange() == null);

        presenter.setWidthRange(10, 111, 10);
        assertArrayEquals(pendingPreset.getCandidate().getWidthRange(), new int[] {10, 111, 10});

        presenter.setHeightRange(100, 10, -20);
        verify(ui, times(1)).onError(ChooseSizeAndCountPresenter.Error.INCORRECT_HEIGHT_RANGE);
        assertTrue(pendingPreset.getCandidate().getHeightRange() == null);

        presenter.setHeightRange(100, 10, 20);
        assertArrayEquals(pendingPreset.getCandidate().getHeightRange(), new int[] {100, 10, 20});

        verifyNoMoreInteractions(ui);
    }
}
