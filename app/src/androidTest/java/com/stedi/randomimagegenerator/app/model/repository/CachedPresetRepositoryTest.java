package com.stedi.randomimagegenerator.app.model.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;
import android.util.SparseArray;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.Utils;

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

    public static class FakeTarget implements PresetRepository {
        private final SparseArray<Preset> fake = new SparseArray<>();

        @Override
        public void save(@NonNull Preset preset) throws Exception {
            fake.put(preset.getId(), preset);
        }

        @Override
        public void remove(int id) throws Exception {
            fake.remove(id);
        }

        @Nullable
        @Override
        public Preset get(int id) throws Exception {
            return fake.get(id);
        }

        @NonNull
        @Override
        public List<Preset> getAll() throws Exception {
            return Utils.sparseArrayToList(fake);
        }
    }

    @Before
    public void before() throws Exception {
        target = spy(new FakeTarget());
        cachedPresetRepository = new CachedPresetRepository(target);
    }

    @Test
    public void test1() throws Exception {
        Preset preset = createPresetWithId(10);

        cachedPresetRepository.remove(20);
        verify(target, times(1)).remove(20);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(10) == preset);

        cachedPresetRepository.remove(10);
        verify(target, times(1)).remove(10);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertNull(cachedPresetRepository.get(10));
        verify(target, times(1)).get(10);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertTrue(cachedPresetRepository.getAll().isEmpty());
        verify(target, times(1)).getAll();
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertTrue(cachedPresetRepository.getAll().isEmpty());
        verifyZeroInteractions(target);
    }

    @Test
    public void test2() throws Exception {
        Preset preset = createPresetWithId(10);

        assertTrue(cachedPresetRepository.get(20) == null);
        verify(target, times(1)).get(20);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(10) == preset);

        assertTrue(cachedPresetRepository.get(10) == preset);
        verifyZeroInteractions(target);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(10) == preset);

        assertTrue(cachedPresetRepository.getAll().size() == 1);
        verify(target, times(1)).getAll();
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(10) == preset);

        assertTrue(cachedPresetRepository.getAll().size() == 1);
        verifyZeroInteractions(target);

        cachedPresetRepository.remove(10);
        verify(target, times(1)).remove(10);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertNull(cachedPresetRepository.get(10));
        verify(target, times(1)).get(10);
        assertTrue(cachedPresetRepository.getCache().size() == 0);
    }

    @Test
    public void test3() throws Exception {
        Preset preset = createPresetWithId(10);

        assertTrue(cachedPresetRepository.getAll().size() == 0);
        verify(target, times(1)).getAll();
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(10) == preset);

        assertTrue(cachedPresetRepository.getAll().size() == 1);
        verify(target, times(1)).getAll();
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(10) == preset);

        assertTrue(cachedPresetRepository.getAll().size() == 1);
        verifyZeroInteractions(target);

        assertTrue(cachedPresetRepository.get(10) == preset);
        verifyZeroInteractions(target);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().get(10) == preset);

        cachedPresetRepository.remove(10);
        verify(target, times(1)).remove(10);
        assertTrue(cachedPresetRepository.getCache().size() == 0);

        assertNull(cachedPresetRepository.get(10));
        verify(target, times(1)).get(10);
        assertTrue(cachedPresetRepository.getCache().size() == 0);
    }

    @Test
    public void test4() throws Exception {
        FakeTarget fakeTarget = new FakeTarget();
        for (int i = 0; i < 10; i++) {
            Preset preset = createPresetWithId(i + 1);
            fakeTarget.save(preset);
        }

        target = spy(fakeTarget);
        cachedPresetRepository = new CachedPresetRepository(target);

        Preset preset = createPresetWithId(20);

        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().indexOfKey(20) >= 0);

        cachedPresetRepository.remove(1);
        verify(target, times(1)).remove(1);
        assertTrue(cachedPresetRepository.getCache().size() == 1);
        assertTrue(cachedPresetRepository.getCache().indexOfKey(20) >= 0);

        assertNotNull(cachedPresetRepository.get(2));
        verify(target, times(1)).get(2);
        assertTrue(cachedPresetRepository.getCache().size() == 2);
        assertTrue(cachedPresetRepository.getCache().indexOfKey(20) >= 0);
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
        assertNotNull(cachedPresetRepository.get(20));
        verifyZeroInteractions(target);

        preset = createPresetWithId(30);
        cachedPresetRepository.save(preset);
        verify(target, times(1)).save(preset);

        assertTrue(cachedPresetRepository.getAll().size() == 11);
        verifyZeroInteractions(target);

        cachedPresetRepository.remove(2);
        verify(target, times(1)).remove(2);

        assertTrue(cachedPresetRepository.getAll().size() == 10);
        verifyZeroInteractions(target);
    }

    private Preset createPresetWithId(int id) {
        Preset preset = new Preset("name",
                GeneratorParams.createDefaultParams(GeneratorType.COLORED_NOISE),
                Quality.png(), "path");
        preset.setId(id);
        return preset;
    }
}
