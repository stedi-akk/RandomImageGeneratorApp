package com.stedi.randomimagegenerator.app.model.repository;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.other.logger.SoutLogger;

import static junit.framework.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DatabasePresetRepositoryTest {
    private static final String DATABASE_NAME = "presets_database_test";

    private static List<GeneratorType> nonEffectTypes = new ArrayList<>();
    private static List<GeneratorType> effectTypes = new ArrayList<>();

    private DatabasePresetRepository repository;

    @BeforeClass
    public static void beforeClass() {
        for (GeneratorType gt : GeneratorType.values()) {
            if (gt.isEffect()) {
                effectTypes.add(gt);
            } else {
                nonEffectTypes.add(gt);
            }
        }
    }

    @Before
    public void before() {
        repository = new DatabasePresetRepository(InstrumentationRegistry.getTargetContext(), DATABASE_NAME, new SoutLogger("DatabasePresetRepositoryTest"));
    }

    @After
    public void after() {
        if (InstrumentationRegistry.getTargetContext().deleteDatabase(DATABASE_NAME)) {
            System.out.println(DATABASE_NAME + " database successfully deleted");
        } else {
            System.out.println("failed to delete " + DATABASE_NAME);
        }
    }

    @Test
    public void simpleTest() throws Exception {
        long timestamp = System.currentTimeMillis();
        Preset preset = new Preset("name", GeneratorParams.createDefaultParams(GeneratorType.FLAT_COLOR), Quality.png(), "folder");
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
    public void nonEffectParamsTest() throws Exception {
        int ids = 1;
        for (GeneratorType type : nonEffectTypes) {
            Preset preset = new Preset("name", GeneratorParams.createDefaultParams(type), Quality.png(), "folder");

            repository.save(preset);
            assertTrue(preset.getId() == ids);

            Preset databasePreset = repository.get(ids);
            assertNotNull(databasePreset);
            assertEquals(preset, databasePreset);

            ids++;
        }

        List<Preset> presets = repository.getAll();
        assertTrue(presets.size() == nonEffectTypes.size());

        for (Preset preset : presets) {
            repository.remove(preset.getId());
        }

        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    public void effectParamsTest() throws Exception {
        int ids = 1;
        for (GeneratorType effectType : effectTypes) {
            for (GeneratorType nonEffecttype : nonEffectTypes) {
                Preset preset = new Preset("name", GeneratorParams.createDefaultEffectParams(effectType, GeneratorParams.createDefaultParams(nonEffecttype)), Quality.png(), "folder");

                repository.save(preset);
                assertTrue(preset.getId() == ids);

                Preset databasePreset = repository.get(ids);
                assertNotNull(databasePreset);
                assertEquals(preset, databasePreset);

                ids++;
            }
        }

        List<Preset> presets = repository.getAll();
        assertTrue(presets.size() == (nonEffectTypes.size() * effectTypes.size()));

        for (Preset preset : presets) {
            repository.remove(preset.getId());
        }

        assertTrue(repository.getAll().isEmpty());
    }
}
