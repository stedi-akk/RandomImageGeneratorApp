package com.stedi.randomimagegenerator.app.model.repository;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.TestUtils;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabasePresetRepositoryTest {
    private DatabasePresetRepository repository;

    @Before
    public void before() {
        TestUtils.deletePresetDatabase();
        repository = new DatabasePresetRepository(InstrumentationRegistry.getTargetContext(), new SoutLogger("DatabasePresetRepositoryTest"));
    }

    @Test
    public void simpleTest() throws Exception {
        long timestamp = System.currentTimeMillis();
        Preset preset = new Preset("name", GeneratorParams.Companion.createDefaultParams(GeneratorType.FLAT_COLOR), Quality.png(), "folder");
        preset.setTimestamp(timestamp);
        preset.setWidth(666);
        preset.setHeight(999);
        preset.setCount(777);

        repository.save(preset);
        assertTrue(preset.getId() == 1);

        Preset databasePreset = repository.get(1);
        assertNotNull(databasePreset);
        assertEquals(preset, databasePreset);

        repository.remove(1);
        databasePreset = repository.get(1);
        assertNull(databasePreset);

        repository.save(preset);
        assertTrue(preset.getId() == 2);

        List<Preset> presets = repository.getAll();
        assertTrue(presets.size() == 1);
        assertTrue(presets.get(0).equals(preset));
        assertTrue(presets.get(0).getId() == 2);
    }

    @Test
    public void saveUpdateTest() throws Exception {
        Preset preset = new Preset("name", GeneratorParams.Companion.createDefaultParams(GeneratorType.FLAT_COLOR), Quality.png(), "folder");

        repository.save(preset);
        Preset databasePreset = repository.get(1);
        assertEquals(databasePreset, preset);

        preset.setName("changed name");
        repository.save(preset);
        databasePreset = repository.get(1);
        assertEquals(databasePreset, preset);
    }

    @Test
    public void nonEffectParamsTest() throws Exception {
        int ids = 1;
        for (GeneratorType type : GeneratorType.Companion.getNON_EFFECT_TYPES()) {
            Preset preset = new Preset("name", GeneratorParams.Companion.createDefaultParams(type), Quality.png(), "folder");

            repository.save(preset);
            assertTrue(preset.getId() == ids);

            Preset databasePreset = repository.get(ids);
            assertNotNull(databasePreset);
            assertEquals(preset, databasePreset);

            ids++;
        }

        List<Preset> presets = repository.getAll();
        assertTrue(presets.size() == GeneratorType.Companion.getNON_EFFECT_TYPES().length);

        for (Preset preset : presets) {
            repository.remove(preset.getId());
        }

        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    public void effectParamsTest() throws Exception {
        int ids = 1;
        for (GeneratorType effectType : GeneratorType.Companion.getEFFECT_TYPES()) {
            for (GeneratorType nonEffecttype : GeneratorType.Companion.getNON_EFFECT_TYPES()) {
                Preset preset = new Preset("name", GeneratorParams.Companion.createDefaultEffectParams(effectType, GeneratorParams.Companion.createDefaultParams(nonEffecttype)), Quality.png(), "folder");

                repository.save(preset);
                assertTrue(preset.getId() == ids);

                Preset databasePreset = repository.get(ids);
                assertNotNull(databasePreset);
                assertEquals(preset, databasePreset);

                ids++;
            }
        }

        List<Preset> presets = repository.getAll();
        assertTrue(presets.size() == (GeneratorType.Companion.getNON_EFFECT_TYPES().length * GeneratorType.Companion.getEFFECT_TYPES().length));

        for (Preset preset : presets) {
            repository.remove(preset.getId());
        }

        assertTrue(repository.getAll().isEmpty());
    }
}
