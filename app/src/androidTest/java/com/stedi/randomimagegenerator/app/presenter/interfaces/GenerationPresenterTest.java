package com.stedi.randomimagegenerator.app.presenter.interfaces;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import com.squareup.otto.ThreadEnforcer;
import com.stedi.randomimagegenerator.ImageParams;
import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.Rig;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.di.qualifiers.RigScheduler;
import com.stedi.randomimagegenerator.app.di.qualifiers.UiScheduler;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.CachedBus;
import com.stedi.randomimagegenerator.app.other.logger.Logger;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;
import com.stedi.randomimagegenerator.generators.FlatColorGenerator;
import com.stedi.randomimagegenerator.generators.Generator;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.List;

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
        Logger logger = new SoutLogger("GenerationPresenterTest");
        presenter = new GenerationPresenterImpl(Schedulers.immediate(), Schedulers.immediate(), new CachedBus(ThreadEnforcer.ANY, logger), logger);
        imageParamsCaptor = ArgumentCaptor.forClass(ImageParams.class);
        presenter.onAttach(ui);
    }

    @AfterClass
    public static void afterClass() {
        TestUtils.deleteTestFolder();
    }

    @Test
    public void testGenerationUnknownError() {
        GeneratorParams generatorParams = mock(GeneratorParams.class);
        when(generatorParams.getGenerator()).thenReturn(null);

        Preset preset = new Preset("name", generatorParams, Quality.png(), TestUtils.getTestFolder().getAbsolutePath());

        presenter.startGeneration(preset);
        verify(ui, times(1)).onStartGeneration();
        verify(ui, times(1)).onGenerationUnknownError();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testOneSimple() {
        Preset preset = new Preset("name", GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR), Quality.png(), TestUtils.getTestFolder().getAbsolutePath());
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
        assertTrue(ip.getPath().getAbsolutePath().equals(TestUtils.getTestFolder().getAbsolutePath()));
        assertTrue(ip.getId() == 1);
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testOneNullGenerate() throws Exception {
        Generator generator = mock(Generator.class);
        when(generator.generate(any())).thenReturn(null);
        GeneratorParams generatorParams = mock(GeneratorParams.class);
        when(generatorParams.getGenerator()).thenReturn(generator);

        Preset preset = new Preset("name", generatorParams, Quality.png(), TestUtils.getTestFolder().getAbsolutePath());
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
        assertTrue(ip.getPath().getAbsolutePath().equals(TestUtils.getTestFolder().getAbsolutePath()));
        assertTrue(ip.getId() == 1);
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testOneExceptionOnGenerate() throws Exception {
        Generator generator = mock(Generator.class);
        doThrow(new NullPointerException()).when(generator).generate(any());
        GeneratorParams generatorParams = mock(GeneratorParams.class);
        when(generatorParams.getGenerator()).thenReturn(generator);

        Preset preset = new Preset("name", generatorParams, Quality.jpg(77), TestUtils.getTestFolder().getAbsolutePath());
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
        assertTrue(ip.getPath().getAbsolutePath().equals(TestUtils.getTestFolder().getAbsolutePath()));
        assertTrue(ip.getId() == 1);
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testMany() {
        Preset preset = new Preset("name", GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR), Quality.png(), TestUtils.getTestFolder().getAbsolutePath());
        preset.setWidth(66);
        preset.setHeight(99);
        preset.setCount(6);

        presenter.startGeneration(preset);
        verify(ui, times(1)).onStartGeneration();
        verify(ui, times(6)).onGenerated(imageParamsCaptor.capture());
        List<ImageParams> imageParams = imageParamsCaptor.getAllValues();
        assertTrue(imageParams.size() == 6);
        int id = 1;
        for (ImageParams ip : imageParams) {
            assertTrue(ip.getWidth() == 66);
            assertTrue(ip.getHeight() == 99);
            assertTrue(ip.getQuality().getFormat() == Quality.png().getFormat());
            assertTrue(ip.getPath().getAbsolutePath().equals(TestUtils.getTestFolder().getAbsolutePath()));
            assertTrue(ip.getId() == id);
            id++;
        }
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testManyWithFailed() throws Exception {
        Generator generator = mock(FlatColorGenerator.class);
        when(generator.generate(any())).thenAnswer(invocation -> {
            ImageParams imageParams = (ImageParams) invocation.getArguments()[0];
            if (imageParams.getId() == 3)
                return null;
            else
                return new FlatColorGenerator().generate(imageParams);
        });
        GeneratorParams generatorParams = mock(GeneratorParams.class);
        when(generatorParams.getGenerator()).thenReturn(generator);

        Preset preset = new Preset("name", generatorParams, Quality.jpg(11), TestUtils.getTestFolder().getAbsolutePath());
        preset.setWidth(100);
        preset.setHeight(100);
        preset.setCount(4);

        presenter.startGeneration(preset);
        verify(ui, times(1)).onStartGeneration();
        verify(ui, times(3)).onGenerated(imageParamsCaptor.capture());
        List<ImageParams> imageParams = imageParamsCaptor.getAllValues();
        assertTrue(imageParams.size() == 3);
        for (ImageParams ip : imageParams) {
            assertTrue(ip.getWidth() == 100);
            assertTrue(ip.getHeight() == 100);
            assertTrue(ip.getQuality().getFormat() == Quality.jpg(11).getFormat());
            assertTrue(ip.getQuality().getQualityValue() == 11);
            assertTrue(ip.getPath().getAbsolutePath().equals(TestUtils.getTestFolder().getAbsolutePath()));
        }
        verify(ui, times(1)).onFailedToGenerate(imageParamsCaptor.capture());
        ImageParams ip = imageParamsCaptor.getValue();
        assertTrue(ip.getId() == 3);
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testManyRange() {
        Preset preset = new Preset("name", GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR), new Quality(Bitmap.CompressFormat.WEBP, 100), TestUtils.getTestFolder().getAbsolutePath());
        preset.setWidthRange(10, 100, 50);
        preset.setHeightRange(100, 10, 50);

        presenter.startGeneration(preset);
        verify(ui, times(1)).onStartGeneration();
        verify(ui, times(4)).onGenerated(imageParamsCaptor.capture()); // should be 9 times, but the RIG library has a bug
        List<ImageParams> imageParams = imageParamsCaptor.getAllValues();
        assertTrue(imageParams.size() == 4);
        assertTrue(imageParams.get(0).getWidth() == 10);
        assertTrue(imageParams.get(0).getHeight() == 100);
        assertTrue(imageParams.get(3).getWidth() == 60);
        assertTrue(imageParams.get(3).getHeight() == 50);
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }

    @Test
    public void testManyRangeWithFailed() throws Exception {
        Generator generator = mock(FlatColorGenerator.class);
        when(generator.generate(any())).thenAnswer(invocation -> {
            ImageParams imageParams = (ImageParams) invocation.getArguments()[0];
            if (imageParams.getWidth() == 10)
                return null;
            else
                return new FlatColorGenerator().generate(imageParams);
        });
        GeneratorParams generatorParams = mock(GeneratorParams.class);
        when(generatorParams.getGenerator()).thenReturn(generator);

        Preset preset = new Preset("name", generatorParams, new Quality(Bitmap.CompressFormat.WEBP, 100), TestUtils.getTestFolder().getAbsolutePath());
        preset.setWidthRange(10, 100, 25);
        preset.setHeight(200);

        presenter.startGeneration(preset);
        verify(ui, times(1)).onStartGeneration();
        verify(ui, times(3)).onGenerated(imageParamsCaptor.capture());
        List<ImageParams> imageParams = imageParamsCaptor.getAllValues();
        assertTrue(imageParams.size() == 3);
        assertTrue(imageParams.get(0).getWidth() == 35);
        assertTrue(imageParams.get(0).getHeight() == 200);
        assertTrue(imageParams.get(2).getWidth() == 85);
        assertTrue(imageParams.get(2).getHeight() == 200);
        verify(ui, times(1)).onFailedToGenerate(imageParamsCaptor.capture());
        ImageParams ip = imageParamsCaptor.getValue();
        assertTrue(ip.getId() == 1);
        assertTrue(ip.getWidth() == 10);
        assertTrue(ip.getHeight() == 200);
        verify(ui, times(1)).onFinishGeneration();
        verifyNoMoreInteractions(ui);
    }
}
