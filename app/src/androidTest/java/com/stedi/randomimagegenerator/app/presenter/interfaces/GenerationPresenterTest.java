package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;
import com.stedi.randomimagegenerator.generators.Generator;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;

import rx.Scheduler;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("MissingPermission")
@RunWith(AndroidJUnit4.class)
public class GenerationPresenterTest {
    private GenerationPresenter.UIImpl ui;
    private GenerationPresenter presenter;
    private ArgumentCaptor<ImageParams> imageParamsCaptor;

    private static class GenerationPresenterImpl extends GenerationPresenter<GenerationPresenter.UIImpl> {
        GenerationPresenterImpl(@NonNull @RigScheduler Scheduler subscribeOn, @NonNull @UiScheduler Scheduler observeOn, @NonNull CachedBus bus, @NonNull Logger logger) {
            super(subscribeOn, observeOn, bus, logger);
        }
    }

    @Before
    public void before() {
        Rig.enableDebugLogging(true);
        ui = mock(GenerationPresenter.UIImpl.class);
        presenter = new GenerationPresenterImpl(Schedulers.immediate(), Schedulers.immediate(), new CachedBus(ThreadEnforcer.ANY), new SoutLogger("GenerationPresenterTest"));
        imageParamsCaptor = ArgumentCaptor.forClass(ImageParams.class);
        presenter.onAttach(ui);
    }

    @AfterClass
    public static void afterClass() {
        try {
            Utils.deleteRecursively(getTestFolder());
            System.out.println("test folder successfully deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOneSimple() {
        Preset preset = new Preset("name", GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR), Quality.png(), getTestFolder().getAbsolutePath());
        preset.setWidth(100);
        preset.setHeight(200);
        preset.setCount(1);

        presenter.startGeneration(preset);
        verify(ui, times(1)).onStartGeneration();
        verify(ui, times(1)).onGenerated(imageParamsCaptor.capture());
        ImageParams ip = imageParamsCaptor.getValue();
        assertTrue(ip.getWidth() == 100);
        assertTrue(ip.getHeight() == 200);
        assertTrue(ip.getQuality().getFormat() == Quality.png().getFormat());
        assertTrue(ip.getPath().getAbsolutePath().equals(getTestFolder().getAbsolutePath()));
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testOneNullGenerate() throws Exception {
        Generator generator = mock(Generator.class);
        when(generator.generate(any())).thenReturn(null);
        GeneratorParams generatorParams = mock(GeneratorParams.class);
        when(generatorParams.getGenerator()).thenReturn(generator);

        Preset preset = new Preset("name", generatorParams, Quality.png(), getTestFolder().getAbsolutePath());
        preset.setWidth(100);
        preset.setHeight(200);
        preset.setCount(1);

        presenter.startGeneration(preset);
        verify(ui, times(1)).onStartGeneration();
        verify(ui, times(1)).onFailedToGenerate(imageParamsCaptor.capture());
        ImageParams ip = imageParamsCaptor.getValue();
        assertTrue(ip.getWidth() == 100);
        assertTrue(ip.getHeight() == 200);
        assertTrue(ip.getQuality().getFormat() == Quality.png().getFormat());
        assertTrue(ip.getPath().getAbsolutePath().equals(getTestFolder().getAbsolutePath()));
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testOneExceptionOnGenerate() throws Exception {
        Generator generator = mock(Generator.class);
        doThrow(new NullPointerException()).when(generator).generate(any());
        GeneratorParams generatorParams = mock(GeneratorParams.class);
        when(generatorParams.getGenerator()).thenReturn(generator);

        Preset preset = new Preset("name", generatorParams, Quality.jpg(77), getTestFolder().getAbsolutePath());
        preset.setWidth(666);
        preset.setHeight(777);
        preset.setCount(1);

        presenter.startGeneration(preset);
        verify(ui, times(1)).onStartGeneration();
        verify(ui, times(1)).onFailedToGenerate(imageParamsCaptor.capture());
        ImageParams ip = imageParamsCaptor.getValue();
        assertTrue(ip.getWidth() == 666);
        assertTrue(ip.getHeight() == 777);
        assertTrue(ip.getQuality().getFormat() == Quality.jpg(77).getFormat());
        assertTrue(ip.getQuality().getQualityValue() == 77);
        assertTrue(ip.getPath().getAbsolutePath().equals(getTestFolder().getAbsolutePath()));
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    private static File getTestFolder() {
        return new File(InstrumentationRegistry.getTargetContext().getApplicationInfo().dataDir, "tests");
    }
}
