package com.stedi.randomimagegenerator.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import com.stedi.randomimagegenerator.Quality;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams;
import com.stedi.randomimagegenerator.app.model.repository.DatabasePresetRepository;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.fail;

public final class TestUtils {
    private TestUtils() {
    }

    @NonNull
    public static File getTestFolder() {
        return new File(InstrumentationRegistry.getTargetContext().getApplicationInfo().dataDir, "tests");
    }

    public static void deleteTestFolder() {
        try {
            deleteRecursively(TestUtils.getTestFolder());
            System.out.println("test folder successfully deleted");
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to delete test folder");
        }
    }

    public static void deletePresetDatabase() {
        Context context = InstrumentationRegistry.getTargetContext();
        File databaseFile = context.getDatabasePath(DatabasePresetRepository.DATABASE_NAME);
        if (!databaseFile.exists() || context.deleteDatabase(databaseFile.getName())) {
            System.out.println("preset database successfully deleted");
        } else {
            fail("failed to delete preset database");
        }
    }

    @NonNull
    public static Preset newSimplePreset() {
        return newSimplePreset(GeneratorParams.Companion.createDefaultParams(GeneratorType.FLAT_COLOR));
    }


    @NonNull
    public static Preset newSimplePreset(@NonNull GeneratorParams params) {
        Preset preset = new Preset("name", params, Quality.png(), TestUtils.getTestFolder().getAbsolutePath());
        preset.setTimestamp(System.currentTimeMillis());
        return preset;
    }

    private static void deleteRecursively(File fileOrDirectory) throws IOException {
        if (!fileOrDirectory.exists()) {
            return;
        }

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursively(child);
            }
        }

        if (!fileOrDirectory.delete()) {
            throw new IOException("failed to delete: " + fileOrDirectory.getAbsolutePath());
        }
    }
}
