package com.stedi.randomimagegenerator.app.presenter.impl;

import android.graphics.Bitmap;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.PendingPreset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(AndroidJUnit4.class)
public class ChooseSaveOptionsPresenterImplTest {
    private ChooseSaveOptionsPresenterImpl presenter;
    private PendingPreset pendingPreset;

    private ChooseSaveOptionsPresenterImpl.UIImpl ui;

    @Before
    public void before() {
        pendingPreset = new PendingPreset();
        pendingPreset.prepareCandidateFrom(TestUtils.newSimplePreset());
        presenter = new ChooseSaveOptionsPresenterImpl(pendingPreset);
        ui = mock(ChooseSaveOptionsPresenterImpl.UIImpl.class);
    }

    @Test
    public void test() {
        pendingPreset.getCandidate().setQuality(new Quality(Bitmap.CompressFormat.JPEG, 77));

        presenter.onAttach(ui);

        presenter.getValues();
        verify(ui, times(1)).showQualityFormat(Bitmap.CompressFormat.JPEG);
        verify(ui, times(1)).showQualityValue(77);

        presenter.setQualityFormat(Bitmap.CompressFormat.PNG);
        verify(ui, times(1)).showQualityValue(100);
        assertTrue(pendingPreset.getCandidate().getQuality().getFormat() == Bitmap.CompressFormat.PNG);
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 100);

        presenter.setQualityValue(99);
        verify(ui, times(1)).onIncorrectQualityValue();
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 100);

        presenter.setQualityValue(100);
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 100);

        presenter.setQualityFormat(Bitmap.CompressFormat.WEBP);
        assertTrue(pendingPreset.getCandidate().getQuality().getFormat() == Bitmap.CompressFormat.WEBP);
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 100);

        presenter.setQualityValue(-1);
        verify(ui, times(2)).onIncorrectQualityValue();
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 100);

        presenter.setQualityValue(120);
        verify(ui, times(3)).onIncorrectQualityValue();
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 100);

        presenter.setQualityValue(99);
        assertTrue(pendingPreset.getCandidate().getQuality().getFormat() == Bitmap.CompressFormat.WEBP);
        assertTrue(pendingPreset.getCandidate().getQuality().getQualityValue() == 99);

        verifyNoMoreInteractions(ui);
    }
}
