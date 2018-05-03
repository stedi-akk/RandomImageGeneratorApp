package com.stedi.randomimagegenerator.app.presenter.impl;

import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.FailedCountGenerator;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.LockedBus;
import com.stedi.randomimagegenerator.app.presenter.interfaces.GenerationPresenter;
import com.stedi.randomimagegenerator.generators.FlatColorGenerator;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import rx.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class GenerationPresenterImplTest {
    private GenerationPresenter.UIImpl ui;
    private GenerationPresenterImpl presenter;
    private LockedBus bus;

    @Before
    public void before() {
        Rig.enableDebugLogging(true);
        bus = new LockedBus(ThreadEnforcer.ANY);
        presenter = new GenerationPresenterImpl(Schedulers.immediate(), Schedulers.immediate(), bus);
        ui = mock(GenerationPresenter.UIImpl.class);
    }

    @AfterClass
    public static void afterClass() {
        TestUtils.deleteTestFolder();
    }

    @Test
    public void testGenerationFailed() {
        presenter.onAttach(ui);

        GeneratorParams generatorParams = mock(GeneratorParams.class);
        when(generatorParams.getGenerator()).thenReturn(null);

        Preset preset = TestUtils.newSimplePreset(generatorParams);

        bus.unlock();
        presenter.startGeneration(preset);
        verify(ui, times(1)).onResult(0, 0);
        verify(ui, times(1)).onGenerationFailed();

        bus.lock();
        presenter.startGeneration(preset);
        verifyZeroInteractions(ui);

        bus.unlock();
        verify(ui, times(1)).onResult(0, 0);
        verify(ui, times(1)).onGenerationFailed();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testGenerationResultUnlockedBus() {
        presenter.onAttach(ui);

        GeneratorParams generatorParams = mock(GeneratorParams.class);
        FailedCountGenerator failedCountGenerator = new FailedCountGenerator(new FlatColorGenerator());
        failedCountGenerator.setToFail(2);
        when(generatorParams.getGenerator()).thenReturn(failedCountGenerator);

        Preset preset = TestUtils.newSimplePreset(generatorParams);
        preset.setCount(5);

        bus.unlock();
        presenter.startGeneration(preset);
        verify(ui, times(2)).imageGenerated(any(), any());
        verify(ui, times(2)).imageSaved(any(), any());
        verify(ui, times(1)).onResult(1, 0);
        verify(ui, times(1)).onResult(2, 0);
        verify(ui, times(1)).onResult(2, 1);
        verify(ui, times(1)).onResult(2, 2);
        verify(ui, times(1)).onResult(2, 3);
        verify(ui, times(1)).onFinishGeneration();

        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testGenerationResultLockedBus() {
        presenter.onAttach(ui);

        GeneratorParams generatorParams = mock(GeneratorParams.class);
        FailedCountGenerator failedCountGenerator = new FailedCountGenerator(new FlatColorGenerator());
        failedCountGenerator.setToFail(2);
        when(generatorParams.getGenerator()).thenReturn(failedCountGenerator);

        Preset preset = TestUtils.newSimplePreset(generatorParams);
        preset.setCount(5);

        bus.lock();
        presenter.startGeneration(preset);
        verify(ui, times(2)).imageGenerated(any(), any());
        verify(ui, times(2)).imageSaved(any(), any());
        verifyZeroInteractions(ui);

        bus.unlock();
        verify(ui, times(1)).onResult(2, 3);
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }
}
