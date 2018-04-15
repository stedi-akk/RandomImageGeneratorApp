package com.stedi.randomimagegenerator.app.presenter.impl;

import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class ChooseSaveOptionsPresenterImplTest {
    private ChooseSaveOptionsPresenterImpl presenter;
    private PendingPreset pendingPreset;
    private Logger logger;

    private ChooseSaveOptionsPresenterImpl.UIImpl ui;

    @Before
    public void before() {
        logger = new SoutLogger("ChooseSaveOptionsPresenterImplTest");
        pendingPreset = new PendingPreset(logger);
        pendingPreset.prepareCandidateFrom(TestUtils.newSimplePreset());
        presenter = new ChooseSaveOptionsPresenterImpl(pendingPreset, logger);
        ui = mock(ChooseSaveOptionsPresenterImpl.UIImpl.class);
    }

    @Test
    public void test() {
        pendingPreset.getCandidate().setQuality(new Quality(Bitmap.CompressFormat.JPEG, 77));
        presenter.onAttach(ui);
        presenter.getData();
        verify(ui, times(1)).showQualityFormat(Bitmap.CompressFormat.JPEG);
        verify(ui, times(1)).showQualityValue(77);

        presenter.setQualityFormat(Bitmap.CompressFormat.WEBP);
        assertTrue(pendingPreset.getCandidate().getQuality().getFormat() == Bitmap.CompressFormat.WEBP);
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 77);

        presenter.setQualityValue(-1);
        verify(ui, times(1)).onIncorrectQualityValue();
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 77);
        presenter.setQualityValue(120);
        verify(ui, times(2)).onIncorrectQualityValue();
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 77);

        presenter.setQualityValue(99);
        assertTrue(pendingPreset.getCandidate().getQuality().getFormat() == Bitmap.CompressFormat.WEBP);
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 99);

        verifyNoMoreInteractions(ui);
    }
}
