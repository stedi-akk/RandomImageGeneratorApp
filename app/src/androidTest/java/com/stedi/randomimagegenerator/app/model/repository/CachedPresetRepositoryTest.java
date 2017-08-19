package com.stedi.randomimagegenerator.app.model.repository;

import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.Preset;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class CachedPresetRepositoryTest {
    private PresetRepository target;
    private CachedPresetRepository cachedPresetRepository;

    @Before
    public void before() throws Exception {
        target = spy(new FakePresetRepository(0));
        cachedPresetRepository = new CachedPresetRepository(target);
    }

    @Test
    public void test1() throws Exception {
        Preset preset = TestUtils.newSimplePreset();

        cachedPresetRepository.remove(20);
        verify(target, times(1)).remove(20);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(1) == preset);

        cachedPresetRepository.remove(1);
        verify(target, times(1)).remove(1);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertNull(cachedPresetRepository.get(1));
        verify(target, times(1)).get(1);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertTrue(cachedPresetRepository.getAll().isEmpty());
        verify(target, times(1)).getAll();
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertTrue(cachedPresetRepository.getAll().isEmpty());
        verifyZeroInteractions(target);
    }

    @Test
    public void test2() throws Exception {
        Preset preset = TestUtils.newSimplePreset();

        assertTrue(cachedPresetRepository.get(20) == null);
        verify(target, times(1)).get(20);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(1) == preset);

        assertTrue(cachedPresetRepository.get(1) == preset);
        verifyZeroInteractions(target);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(1) == preset);

        assertTrue(cachedPresetRepository.getAll().size() == 1);
        verify(target, times(1)).getAll();
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(1) == preset);

        assertTrue(cachedPresetRepository.getAll().size() == 1);
        verifyZeroInteractions(target);

        cachedPresetRepository.remove(1);
        verify(target, times(1)).remove(1);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertNull(cachedPresetRepository.get(1));
        verify(target, times(1)).get(1);
        assertTrue(cachedPresetRepository.getCache().size() == 0);
    }

    @Test
    public void test3() throws Exception {
        Preset preset = TestUtils.newSimplePreset();

        assertTrue(cachedPresetRepository.getAll().size() == 0);
        verify(target, times(1)).getAll();
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(1) == preset);

        assertTrue(cachedPresetRepository.getAll().size() == 1);
        verify(target, times(1)).getAll();
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(1) == preset);

        assertTrue(cachedPresetRepository.getAll().size() == 1);
        verifyZeroInteractions(target);

        assertTrue(cachedPresetRepository.get(1) == preset);
        verifyZeroInteractions(target);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(1) == preset);

        cachedPresetRepository.remove(1);
        verify(target, times(1)).remove(1);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertNull(cachedPresetRepository.get(1));
        verify(target, times(1)).get(1);
        assertTrue(cachedPresetRepository.getCache().size() == 0);
    }

    @Test
    public void test4() throws Exception {
        target = spy(new FakePresetRepository(10));
        cachedPresetRepository = new CachedPresetRepository(target);

        Preset preset = TestUtils.newSimplePreset();

        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().indexOfKey(11) >= 0);

        cachedPresetRepository.remove(1);
        verify(target, times(1)).remove(1);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().indexOfKey(11) >= 0);

        assertNotNull(cachedPresetRepository.get(2));
        verify(target, times(1)).get(2);
        assertTrue(cachedPresetRepository.getCache().size() == 2);
        assertTrue(cachedPresetRepository.getCache().indexOfKey(11) >= 0);
        assertTrue(cachedPresetRepository.getCache().indexOfKey(2) >= 0);

        List<Preset> allPresets = cachedPresetRepository.getAll();
        verify(target, times(1)).getAll();
        assertTrue(cachedPresetRepository.getCache().size() == 10);
        assertTrue(allPresets.size() == 10);

        assertTrue(cachedPresetRepository.getAll().size() == 10);
        verifyZeroInteractions(target);

        assertNotNull(cachedPresetRepository.get(2));
        assertNotNull(cachedPresetRepository.get(3));
        assertNotNull(cachedPresetRepository.get(4));
        assertNotNull(cachedPresetRepository.get(5));
        assertNotNull(cachedPresetRepository.get(6));
        assertNotNull(cachedPresetRepository.get(7));
        assertNotNull(cachedPresetRepository.get(8));
        assertNotNull(cachedPresetRepository.get(9));
        assertNotNull(cachedPresetRepository.get(10));
        assertNotNull(cachedPresetRepository.get(11));
        verifyZeroInteractions(target);

        preset = TestUtils.newSimplePreset();
        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);

        assertTrue(cachedPresetRepository.getAll().size() == 11);
        verifyZeroInteractions(target);

        cachedPresetRepository.remove(2);
        verify(target, times(1)).remove(2);

        assertTrue(cachedPresetRepository.getAll().size() == 10);
        verifyZeroInteractions(target);
    }
}
